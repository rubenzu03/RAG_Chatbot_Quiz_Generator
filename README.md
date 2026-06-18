# RAG Chatbot & Quiz Generator
A fully local Retrieval-Augmented Generation (RAG) chatbot and automatic quiz generator with no telemetry and usage costs

<img src="https://img.shields.io/badge/License-MIT-green"></img>
<img src="https://img.shields.io/badge/Built%20with-Llama-blueviolet?logo=meta"></img>
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=black"></img>
<img src="https://img.shields.io/badge/Spring%20AI-6DB33F?logo=spring&logoColor=black"></img>
<img src="https://img.shields.io/badge/Powered%20by-Ollama-black?logo=ollama"></img>

## ✨ Features
- Conversational chatbot with contextual memory powered by your own documents
- Automatic quiz generator based on the uploaded content
- 100% local AI using the llama3.2:3b model running via Ollama, no external API calls
- RAG (Retrieval-Augmented Generation) with nomic-embed-text embeddings
- Local vector database (Milvus Standalone) for efficient semantic search
- Object storage with MinIO
- Complete privacy, all data stays on your machine

# Prerequisites
- Docker and Docker Compose
- Ollama installed
- ~8 GB of available RAM
- ~5 GB of disk space

# Installation & Setup
### 1. Install Ollama and pull the models
Download and install Ollama, then run:
```
ollama pull llama3.2:latest
ollama pull nomic-embed-text
```

### 2. Clone the repository
```
git clone https://github.com/rubenzu03/RAG_chatbot.git
cd RAG_chatbot
```

### 3. Configure environment variables and edit the `.env` file with your values
```
cp .env.example .env
```

### 4. Start the containers
```
docker compose up -d
```

### 5. Set up MinIO
 
1. Open [http://localhost:9001](http://localhost:9001) in your browser
2. Log in with the credentials from your `.env` file
3. Create a bucket named **`ragchatbot`**
4. Upload the documents you want to use as the knowledge base (PDF, TXT, DOCX, MD...)

### 6. Re-index the documents
 
After uploading your files, restart the containers so the RAG pipeline processes and indexes the content:
 
```bash
docker compose down
docker compose up -d
```

### 7. Access the application
 
Open [http://localhost:5173](http://localhost:5173)
#### Built with Llama


