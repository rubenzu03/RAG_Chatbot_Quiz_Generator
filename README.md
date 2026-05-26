# Instructions
- Download and install Ollama before starting the Docker containers
- Install the necessary models using:
```
ollama pull llama3.2:latest
ollama pull nomic-embed-text
```
- Copy the env example file and rename it to .env with the desired values
- Run from the project root:

```
docker compose up -d
```
- Log in to http://localhost:9001/login with the MinIO user and password in the env file
- Create a bucket called "ragchatbot"
- Upload the files to the bucket "ragchatbot"
- Restart the Docker containers running from the project root:
```
docker compose down
docker compose up -d
```
- Access the frontend at http://localhost:5173/
#### Built with Llama


