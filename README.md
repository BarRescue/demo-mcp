# Cursus MCP Server (demo)

Spring Boot 4.1 + Spring AI 2.0 MCP-server die hardcoded cursusdata als tools aanbiedt.
Transport: **Streamable HTTP** op `POST /mcp`.

## Tools

| Tool | Wat het doet | Voorbeeldvraag aan Claude |
|---|---|---|
| `zoek_cursussen` | Filtert op categorie, niveau (BEGINNER/GEVORDERD/EXPERT) en/of zoekterm | "Welke AI-cursussen voor beginners zijn er?" |
| `cursus_details` | Alle details van één cursus op code | "Wat kost SPRING-201 en waar is die?" |
| `cursus_categorieen` | Lijst met categorieën | "Op welke gebieden bieden jullie trainingen?" |

Data staat in `CursusRepository` — pas die aan voor je eigen demo.

## Starten

```bash
mvn spring-boot:run
# of
mvn package && java -jar target/cursus-mcp-server-0.0.1-SNAPSHOT.jar
```

Vereist Java 21+.

## Koppelen aan Claude

### Optie A — Claude Code (lokaal, snelst)

```bash
claude mcp add --transport http cursussen http://localhost:8080/mcp
claude mcp list   # moet ✔ Connected tonen
```

### Optie B — Claude (web/desktop) als custom connector

Claude verbindt vanuit de cloud van Anthropic, dus `localhost` werkt niet: de server
moet publiek bereikbaar zijn. Voor een demo is een tunnel het makkelijkst:

```bash
cloudflared tunnel --url http://localhost:8080
# of: ngrok http 8080
```

Daarna in Claude: **Customize → Connectors → + Add → Add custom connector**
- URL: `https://<jouw-tunnel>/mcp`
- Authentication: **No sign in**

## Handmatig testen met curl

```bash
H='-H Content-Type:application/json -H Accept:application/json,text/event-stream'

# 1. initialize (pak de Mcp-Session-Id uit de response headers)
curl -si $H localhost:8080/mcp -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2025-06-18","capabilities":{},"clientInfo":{"name":"curl","version":"1"}}}'

# 2. tools aanroepen
curl -s $H -H "Mcp-Session-Id: <id>" localhost:8080/mcp -d '{"jsonrpc":"2.0","method":"notifications/initialized"}'
curl -s $H -H "Mcp-Session-Id: <id>" localhost:8080/mcp -d '{"jsonrpc":"2.0","id":2,"method":"tools/list"}'
curl -s $H -H "Mcp-Session-Id: <id>" localhost:8080/mcp -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"zoek_cursussen","arguments":{"categorie":"AI"}}}'
```

Of visueel: `npx @modelcontextprotocol/inspector` → Streamable HTTP → `http://localhost:8080/mcp`.

## Let op

Er zit geen authenticatie op — prima voor een demo met een tijdelijke tunnel, niet voor productie.
