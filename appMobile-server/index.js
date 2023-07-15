const express = require('express')

const app = express()
const Router = require('./routes/rutas')
const bodyParser = require('body-parser')

app.use(bodyParser.json({ limit: '50mb' }));
app.use(bodyParser.urlencoded({ limit: '50mb', extended: true }));
app.use('/', Router)
app.use('/imagenes', express.static('imagenes'));

app.get('/imagen/:nombreArchivo', (req, res) => {
  const nombreArchivo = req.params.nombreArchivo;
  res.sendFile(`${__dirname}/imagenes/${nombreArchivo}`);
});

app.listen(3000, () => {
  console.log("Servidor en http://localhost:3000")
})