//JWT (JSON Web Tokens) permite la creación de tokens de seguridad para trasmitir información encriptada entre el servidor y cliente consta de 3 partes:
//1.- cabecera -> Generalmente es Bearer que es usado para trasmitir tokens de autenticación
//2.- datos(payload)
//3.- firma digital -> Nos permitira verificar la autenticidad del token.

//En este archivo.js estoy aplicando la Autenticación Basada en Tokens para ello requiero la librería jsonwebtoken;
//en especifico en este archivo.js estoy verificando si el token existe y verifico su autenticidad con la firma digital si pasa estos filtros entonces tendremos acceso a la ruta del servidor.

const jwt = require('jsonwebtoken');
require('dotenv').config();


module.exports = {
  verificar: (req, res, next) => {
    const token = req.headers.authorization.split(' ')[1]
    if (!token) return res.send({ msg: 'Acceso Denegado', status: 404 })

    try {
      const verificado = jwt.verify(token, process.env.SECRET_KEY)
      req.usuario = verificado
      next()
    } catch (error) {
      res.send({ status: 400, msg: "Token Invalido" })
    }

  }
}