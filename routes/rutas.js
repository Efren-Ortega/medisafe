const express = require('express')
const Router = express.Router()
const Usuario = require('../controlador/ctrlUsuarios')
//const App = require('../controlador/App')

//MÉTODO DE SEGURIDAD PARA PROTEGER CADA RUTA CON JWT
const { verificar } = require('../seguridad/seguridad_JWt')

/*let _App = new App()
*/

let _Usuario = new Usuario()
//RUTAS USUARIOS
Router.post('/usuario/registrarse', _Usuario.crearCuenta)
//Router.get('/usuarios/info/:idPerfil', verificar, _App.obtnerDatosUsuario)

Router.post('/usuarios/auth', _Usuario.iniciarSesion)
Router.post('/usuario/restorePassword', _Usuario.recuperarContrasena)

Router.put('/usuario/guardarContra', _Usuario.guardarContra)
Router.get('/usuarios/usuario/:idPerfil', _Usuario.getUser)
Router.put('/perfil/actualizar/:idPerfil', _Usuario.editarPerfil)


module.exports = Router;