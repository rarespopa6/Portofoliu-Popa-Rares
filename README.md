<div align="center">

# Hi, I'm Rareș 👋

### AI & Backend Engineer — I build AI agents that do real work, in production

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Rare%C8%99%20Popa-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rares-popa-5427b22a8/)
![Based in Romania](https://img.shields.io/badge/Based%20in-Romania%20%F0%9F%87%AA%F0%9F%87%BA-003399?style=for-the-badge)
![Languages](https://img.shields.io/badge/Romanian%20%C2%B7%20English%20C1%20%C2%B7%20Deutsch%20C1-2F2F2F?style=for-the-badge)

</div>

---

## 🧠 What I do

I design and ship **agentic AI systems**: LLM agents with tool calling, MCP servers, RAG and multi-agent workflows. And I build the backend that makes them reliable: distributed workers, multi-tenant auth, caching and observability.

My rule of thumb: **LLMs for understanding, deterministic code for facts.** The model reads, reasons and drafts. Anything that has to be correct, like fiscal checks, identity data or permissions, runs as plain, testable code.

- 🔭 **Now:** AI & Backend Engineer at **Principal33** · AI Engineer at **Webby-Soft**
- 🛠️ **Building:** [Orkis](https://github.com/rarespopa6/Orkis-public), a no-code AI agent platform for business workflows
- 🏆 **Award:** MHP Porsche Big Brains Award for [Syntrly](https://github.com/rarespopa6/Syntrly-Public)
- 🎓 **Studying:** MBA in German at ASE Bucharest, after a B.Sc. in Computer Science (German track) at Babeș-Bolyai

---

## 🚀 Featured projects

<table>
<tr>
<td width="50%" valign="top">

### [Orkis](https://github.com/rarespopa6/Orkis-public)
**No-code AI agent platform for business workflows**

<!-- PREVIEW IMAGE: Orkis dashboard or landing page -->
<a href="https://github.com/rarespopa6/Orkis-public"><img src="assets/orkis-preview.png" alt="Orkis preview" width="100%"></a>

- **Ask Orkis** orchestrates four configurable sub-agents: marketing, meetings, support and operations
- Event-driven pipeline: Pub/Sub + webhooks → Celery workers → tool-calling agents
- Invoice processing with deterministic **ANAF company and VAT checks**
- Non-invasive actions run automatically, invasive ones wait for user approval

`Django` `Celery` `Redis` `PostgreSQL` `React` `TypeScript` `Docker`

🌐 [orkis.ro](https://orkis.ro) · 📄 [Case study](https://github.com/rarespopa6/Orkis-public)

</td>
<td width="50%" valign="top">

### [Syntrly](https://github.com/rarespopa6/Syntrly-Public)
**AI-native email & calendar workspace** · 🏆 MHP Porsche Big Brains Award

<!-- PREVIEW IMAGE: Syntrly inbox + SuperAgent -->
<a href="https://github.com/rarespopa6/Syntrly-Public"><img src="assets/syntrly-preview.png" alt="Syntrly preview" width="100%"></a>

- **SuperAgent** answers questions over Gmail & Outlook data with **pgvector RAG**
- Tool calling for emails and calendar events; actions run only after the user confirms
- Multi-tenant OAuth2 with encrypted token storage, security-first (CASA-minded) design
- Live AI-to-client updates over WebSockets

`Django REST` `React` `PostgreSQL + pgvector` `Redis` `Gemini` `Docker`

📄 [Case study](https://github.com/rarespopa6/Syntrly-Public)

</td>
</tr>
</table>

---

## 🏭 In production

<table>
<tr>
<td align="center"><h3>−40%</h3>login latency</td>
<td align="center"><h3>−60%</h3>DB query spans</td>
<td align="center"><h3>4</h3>engineers onboarded</td>
<td align="center"><h3>5</h3>product verticals supported</td>
</tr>
</table>

**AI & Backend Engineer · Principal33** — Mar 2025 – present · Cluj-Napoca
- Built and deployed a production **AI agent exposed through an MCP server** for identity diagnostics, combining LLMs with deterministic C# code through complex tool-calling loops
- Designed and delivered solo a greenfield **multi-tenant provisioning microservice** with custom M2M authentication and database orchestration
- Built **Redis Pub/Sub distributed cache invalidation** across backend instances, cutting login latency by 40% and database query spans by 60%
- Onboarding buddy for 4 engineers, cross-stack support across 5 product verticals

**AI Engineer · Webby-Soft** — Oct 2025 – present · remote
- LLM Engineering & MLOps: Designed end-to-end data pipelines and offline QLoRA fine-tuning workflows (4-bit quantization) for domain-specific LLMs, including data validation, PII sanitization, and model evaluation under strict hardware and privacy constraints.
- AI Systems Architecture: Architected and delivered a secure, production-grade AI backend integrating LLM inference, structured JSON streaming, and multi-agent workflows into clinician-facing applications, with RBAC, privacy safeguards, and Human-in-the-Loop evaluation.

**Fullstack Software Engineer · Webby-Soft** — Jul 2024 – Mar 2025 · remote
- Built and shipped a commercial food-ordering platform from scratch: real-time order dispatch, secure auth, automated notifications (React Native, Node.js, MongoDB)

---

## 🧩 How I build agents

| Principle | In practice |
|---|---|
| **Deterministic where it matters** | The LLM extracts and explains; checks, permissions and business rules run as code |
| **Human-in-the-loop by design** | Safe actions run end-to-end, invasive ones wait for approval |
| **Scoped tools** | Each agent only sees the tools it is allowed to use |
| **Async by default** | LLM work runs in background workers, never in the request path |
| **Multi-tenant from day one** | Data, configurations and permissions isolated per tenant |

---

## 🛠️ Tech stack

**AI & Agents**
![LLMs](https://img.shields.io/badge/LLMs-6B4FBB?style=flat-square)
![Agentic AI](https://img.shields.io/badge/Agentic%20AI-6B4FBB?style=flat-square)
![MCP](https://img.shields.io/badge/MCP-6B4FBB?style=flat-square)
![Tool Calling](https://img.shields.io/badge/Tool%20Calling-6B4FBB?style=flat-square)
![RAG](https://img.shields.io/badge/RAG-6B4FBB?style=flat-square)
![Multi--Agent](https://img.shields.io/badge/Multi--Agent-6B4FBB?style=flat-square)
![QLoRA](https://img.shields.io/badge/QLoRA%20Fine--tuning-6B4FBB?style=flat-square)
![Context Engineering](https://img.shields.io/badge/Context%20Engineering-6B4FBB?style=flat-square)

**Backend**
![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
![C#](https://img.shields.io/badge/C%23-512BD4?style=flat-square)
![.NET](https://img.shields.io/badge/.NET-512BD4?style=flat-square&logo=dotnet&logoColor=white)
![Django](https://img.shields.io/badge/Django-092E20?style=flat-square&logo=django&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=flat-square&logo=nodedotjs&logoColor=white)
![Celery](https://img.shields.io/badge/Celery-37814A?style=flat-square&logo=celery&logoColor=white)
![OAuth2 / OIDC](https://img.shields.io/badge/OAuth2%20%2F%20OIDC-2F2F2F?style=flat-square)
![Microservices](https://img.shields.io/badge/Microservices-2F2F2F?style=flat-square)

**Data**
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![pgvector](https://img.shields.io/badge/pgvector-4169E1?style=flat-square)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white)

**Frontend**
![React](https://img.shields.io/badge/React-20232A?style=flat-square&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white)
![React Native](https://img.shields.io/badge/React%20Native-20232A?style=flat-square&logo=react&logoColor=61DAFB)

**Cloud & DevOps**
![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=flat-square&logo=kubernetes&logoColor=white)
![CI/CD](https://img.shields.io/badge/CI%2FCD-2F2F2F?style=flat-square)
![Datadog](https://img.shields.io/badge/Datadog-632CA6?style=flat-square&logo=datadog&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white)

---

## 🎓 Education & certifications

| | |
|---|---|
| 🎓 **MBA in German** | Faculty of Business Administration in Foreign Languages (FABIZ), ASE Bucharest · 2026 – 2028 |
| 🎓 **B.Sc. Computer Science** (German-language program) | Babeș-Bolyai University, Cluj-Napoca · 2023 – 2026 |
| ☁️ **AWS Certified Cloud Practitioner** | Amazon Web Services · 2025 |
| 🇩🇪 **Deutsches Sprachdiplom Stufe II** | C1 · 2023 |
| 🇬🇧 **Cambridge English (FCE)** | C1 · 2020 |

---

<div align="center">

**Happy to talk about agentic AI, LLM systems or backend architecture.**

[![LinkedIn](https://img.shields.io/badge/Let's%20connect-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rares-popa-5427b22a8/)

</div>
