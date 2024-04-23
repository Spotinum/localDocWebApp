# Use a Node.js base image
FROM node:latest AS builder
WORKDIR /app
RUN git clone https://github.com/Spotinum/localDocAppVue.git .
RUN npm install
RUN npm run build



FROM node:latest
WORKDIR /app
COPY --from=builder /app/dist ./dist
EXPOSE 9000
CMD ["node", "-e", "const http = require('http'), fs = require('fs'), path = require('path'); http.createServer((req, res) => { const file = fs.createReadStream(path.join(__dirname, 'dist', req.url)); file.on('open', () => file.pipe(res)); file.on('error', () => { res.writeHead(404); res.end(); }); }).listen(9000, () => console.log('Server running on port 9000'));"]
