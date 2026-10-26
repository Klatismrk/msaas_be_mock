# MSaaS BE Mock

Mock aplikace simulující chování **MSaaS (Malware Scanning as a Service)** backend API.  
Slouží vývojářům k testování integrací bez nutnosti připojení na reálnou MSaaS službu.

---

## Obsah

- [Spuštění](#spuštění)
- [Jak mock funguje](#jak-mock-funguje)
- [Konfigurace chování](#konfigurace-chování)
- [API endpointy](#api-endpointy)
  - [POST /files/scan-base64](#post-filesscan-base64)
  - [POST /files/scan-s3](#post-filesscan-s3)
  - [GET /files/{scanId}/status](#get-filesscanidstatus)
  - [GET /files/{scanId}/result](#get-filesscanidresult)
- [Chybové stavy](#chybové-stavy)
- [Pomocné endpointy](#pomocné-endpointy)
- [Typické testovací scénáře](#typické-testovací-scénáře)

---

## Spuštění

**Požadavky:** Java 21+, Maven (nebo použij přiložený `mvnw`)

```bash
./mvnw spring-boot:run
```

Aplikace poběží na `http://localhost:8080`.

---

## Jak mock funguje

Životní cyklus požadavku na skenování probíhá ve třech krocích:

```
1. POST /files/scan-base64  nebo  POST /files/scan-s3
        │
        │  Mock provede vstupní validaci a uloží výsledek do paměti.
        │  Vrátí scanId (UUID).
        ▼
2. GET /files/{scanId}/status  nebo  GET /files/{scanId}/result
        │
        │  Dokud neuplyne nakonfigurovaná doba (duration), mock vrací IN_PROGRESS.
        │  Po uplynutí doby vrátí nakonfigurovaný finalStatus (výchozí: DONE).
        ▼
3. Hotovo – výsledek je dostupný přes GET /result
```

**Stav se uchovává pouze v paměti.** Po restartu aplikace jsou všechny záznamy smazány.

---

## Konfigurace chování

Chování mocku se nastavuje přes REST API za běhu – není třeba restartovat aplikaci.

### Získání aktuální konfigurace

```http
GET /configuration
```

**Příklad odpovědi:**
```json
{
  "finalStatus": "DONE",
  "duration": 5000,
  "maxFileSize": 1000000,
  "validationFailed": false,
  "msaasNotReachable": false,
  "msaasInternalError": false,
  "internalSaveScan500": false,
  "providerResults": [
    { "providerCode": "AV1", "providerName": "Antivirus 1", "valid": true },
    { "providerCode": "AV2", "providerName": "Antivirus 2", "valid": null },
    { "providerCode": "AV3", "providerName": "Antivirus 3", "valid": true }
  ],
  "maxFileNameSize": 255,
  "maxMimeTypeSize": 50
}
```

### Nastavení konfigurace

```http
POST /configuration
Content-Type: application/json
```

Tělo může obsahovat **libovolnou podmnožinu polí** – uvádíš jen to, co chceš změnit. Ostatní hodnoty zůstanou beze změny.

---

### Popis konfiguračních parametrů

| Parametr | Typ | Výchozí | Popis |
|---|---|---|---|
| `finalStatus` | enum | `DONE` | Stav vrácený po dokončení skenování. Možné hodnoty: `DONE`, `ERROR`. |
| `duration` | long (ms) | `5000` | Jak dlouho (v milisekundách) trvá skenování. Po tuto dobu vrací GET endpointy stav `IN_PROGRESS`. |
| `maxFileSize` | long (bytes) | `1000000` | Maximální povolená velikost souboru v bajtech. |
| `validationFailed` | boolean | `false` | Pokud `true`, výsledek skenování bude `valid: false` (soubor neprošel kontrolou). |
| `msaasNotReachable` | boolean | `false` | Pokud `true`, všechny endpointy vrátí HTTP **503 Service Unavailable**. |
| `msaasInternalError` | boolean | `false` | Pokud `true`, všechny endpointy vrátí HTTP **500 Internal Server Error**. |
| `internalSaveScan500` | boolean | `false` | Pokud `true`, POST endpointy selžou při ukládání a vrátí HTTP **500**. |
| `providerResults` | array | viz výše | Seznam AV providerů a jejich výsledků. `valid: null` znamená, že provider nebyl volán. |

> **Pozor:** `maxFileNameSize` (255) a `maxMimeTypeSize` (50) jsou pevně dané a nelze je konfigurací změnit.

---

## API endpointy

### Společné hlavičky

Všechny endpointy pracují s hlavičkou `X-Correlation-Id` – pokud ji pošleš v requestu, mock ji vrátí zpět v response.

```http
X-Correlation-Id: 56663b75-8cea-4be8-81eb-1eca9bb9ce20
```

---

### POST /files/scan-base64

Odešle soubor zakódovaný v Base64 ke skenování.

**Hlavičky requestu:**
```http
Content-Type: application/json
X-Mime-Type: application/pdf        # volitelné, výchozí: application/pdf
X-File-Size: 4096                   # velikost souboru v bajtech, výchozí: 0
```

**Tělo requestu:**
```json
{
  "fileName": "document.pdf",
  "data": "JVBERi0xLjQK...",
  "archivePwd": ["heslo1", "heslo2"]
}
```

| Pole | Povinné | Popis |
|---|---|---|
| `fileName` | ✅ | Název souboru (max. 255 znaků) |
| `data` | ✅ | Obsah souboru zakódovaný v Base64 |
| `archivePwd` | ❌ | Hesla k zašifrovaným archivům |

**Úspěšná odpověď (HTTP 200):**
```json
{
  "scanId": "0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1"
}
```

---

### POST /files/scan-s3

Odešle ke skenování soubor uložený na S3 úložišti.

> **Poznámka:** V mocku se místo skutečné URL používá enum hodnota, která určuje, jak má mock zareagovat.

**Hlavičky requestu:**
```http
Content-Type: application/json
X-Mime-Type: application/pdf        # volitelné, výchozí: application/pdf
X-File-Size: 4096                   # velikost souboru v bajtech, výchozí: 0
```

**Tělo requestu:**
```json
{
  "fileName": "document.pdf",
  "url": "VALID",
  "archivePwd": ["heslo1"]
}
```

| Pole | Povinné | Popis |
|---|---|---|
| `fileName` | ✅ | Název souboru (max. 255 znaků) |
| `url` | ✅ | Simulovaný stav S3 úložiště (viz tabulka níže) |
| `archivePwd` | ❌ | Hesla k zašifrovaným archivům |

**Hodnoty pole `url` a jejich efekt:**

| Hodnota | Simulovaný scénář | HTTP odpověď |
|---|---|---|
| `VALID` | Soubor je dostupný a OK | 200 (nebo 400, pokud je příliš velký) |
| `UNREACHABLE` | S3 úložiště není dosažitelné | 400 (kód 301) |
| `NOT_FOUND` | Soubor na S3 neexistuje | 400 (kód 302) |
| `UNAUTHORIZED` | Přístup k souboru zamítnut | 400 (kód 303) |

**Úspěšná odpověď (HTTP 200):**
```json
{
  "scanId": "0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1"
}
```

---

### GET /files/{scanId}/status

Vrátí aktuální stav skenování.

```http
GET /files/0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1/status
```

**Odpověď během skenování (do uplynutí `duration`):**
```json
{
  "scanId": "0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1",
  "status": "IN_PROGRESS",
  "valid": null
}
```

**Odpověď po dokončení skenování:**
```json
{
  "scanId": "0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1",
  "status": "DONE",
  "valid": true
}
```

**Hodnoty pole `status`:**

| Hodnota | Popis |
|---|---|
| `IN_PROGRESS` | Skenování probíhá |
| `DONE` | Skenování dokončeno |
| `ERROR` | Během skenování nastala chyba |

> `valid` je `null` vždy, kdy stav není `DONE`.

---

### GET /files/{scanId}/result

Vrátí podrobné výsledky skenování včetně výsledků jednotlivých AV providerů.

```http
GET /files/0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1/result
```

**Odpověď během skenování:**
```json
{
  "scanId": "0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1",
  "status": "IN_PROGRESS",
  "valid": null,
  "scannedAt": null,
  "results": []
}
```

**Odpověď po dokončení:**
```json
{
  "scanId": "0e9bffbc-d352-4d78-9dcc-a0a6c46c05b1",
  "status": "DONE",
  "valid": true,
  "scannedAt": "2026-06-08T10:05:00.000+00:00",
  "results": [
    { "providerCode": "AV1", "providerName": "Antivirus 1", "valid": true },
    { "providerCode": "AV2", "providerName": "Antivirus 2", "valid": null },
    { "providerCode": "AV3", "providerName": "Antivirus 3", "valid": true }
  ]
}
```

> `valid: null` u providera znamená, že daný provider soubor nekontroloval.

---

## Chybové stavy

### Formát chybové odpovědi

Všechny chyby vracejí strukturovaný JSON s polem `errors`:

```json
{
  "errors": [
    {
      "code": 202,
      "detail": "Překročena maximální velikost souboru",
      "source": "request.body.data",
      "meta": {
        "errorType": "VALIDATION"
      }
    }
  ]
}
```

### Přehled chybových kódů

| Kód | HTTP | Detail | Zdroj |
|---|---|---|---|
| `1` | 400 | Dlouhý název souboru | `request.body.fileName` |
| `2` | 400 | Dlouhý requiredMimeType | `request.body.requiredMimeType` |
| `3` | 404 | Scan nenalezen | `request.path.scanId` |
| `201` | 400 | Nelze dekódovat obsah souboru | `request.body.data` |
| `202` | 400 | Překročena maximální velikost souboru | `request.body.data` |
| `301` | 400 | Zdrojové s3 úložiště není dostupné | `request.body.fileUrl` |
| `302` | 400 | Zdrojový soubor neexistuje | `request.body.fileUrl` |
| `303` | 400 | Zdrojový soubor není přístupný | `request.body.fileUrl` |
| `304` | 400 | Překročena maximální velikost souboru | `request.body.fileUrl` |

### Systémové chyby (bez struktury)

| HTTP | Situace | Jak vyvolat |
|---|---|---|
| `500` | Interní chyba MSaaS | `msaasInternalError: true` |
| `500` | Chyba při ukládání požadavku | `internalSaveScan500: true` |
| `503` | MSaaS není dostupné | `msaasNotReachable: true` |

---

## Pomocné endpointy

### GET /clear-map

Vymaže všechny záznamy o skenování uložené v paměti. Užitečné pro reset stavu mezi testy.

```http
GET /clear-map
```

**Odpověď:**
```
Map cleared
```

---

## Typické testovací scénáře

### 1. Úspěšné skenování (soubor je OK)

Výchozí konfigurace – není třeba nic měnit.

```http
# 1. Pošli soubor
POST /files/scan-base64
X-File-Size: 100
{ "fileName": "test.pdf", "data": "dGVzdA==" }

# 2. Počkej 5 sekund, pak zkontroluj výsledek
GET /files/{scanId}/result
# → status: DONE, valid: true
```

---

### 2. Skenování selže (soubor obsahuje malware)

```http
# Nastavení konfigurace
POST /configuration
{ "validationFailed": true }

# Teď POST + GET vrátí valid: false
```

---

### 3. Dlouhé skenování (testování polling logiky)

```http
POST /configuration
{ "duration": 30000 }

# Skenování bude trvat 30 sekund – GET po tuto dobu vrací IN_PROGRESS
```

---

### 4. Skenování skončí chybou (status ERROR)

```http
POST /configuration
{ "finalStatus": "ERROR" }
```

---

### 5. MSaaS je nedostupné (testování fallbacku)

```http
POST /configuration
{ "msaasNotReachable": true }

# Všechny endpointy vrátí HTTP 503

# Po otestování vrátit do normálu:
POST /configuration
{ "msaasNotReachable": false }
```

---

### 6. Soubor je příliš velký

```http
POST /configuration
{ "maxFileSize": 100 }

# Pak pošli soubor větší než 100 bajtů → HTTP 400, kód 202
```

---

### 7. S3 úložiště není dostupné

```http
POST /files/scan-s3
X-File-Size: 100
{ "fileName": "test.pdf", "url": "UNREACHABLE" }
# → HTTP 400, kód 301
```

---

### 8. Reset stavu mezi testy

```http
GET /clear-map

POST /configuration
{
  "finalStatus": "DONE",
  "duration": 5000,
  "maxFileSize": 1000000,
  "validationFailed": false,
  "msaasNotReachable": false,
  "msaasInternalError": false,
  "internalSaveScan500": false
}
```

