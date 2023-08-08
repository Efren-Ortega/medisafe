const express = require('express')
const Router = express.Router()
const Usuario = require('../controlador/ctrlUsuarios')
const Paciente = require('../controlador/ctrlPacientes')

//const App = require('../controlador/App')

//MÉTODO DE SEGURIDAD PARA PROTEGER CADA RUTA CON JWT
const { verificar } = require('../seguridad/seguridad_JWt')
const CtrlMedico = require('../controlador/ctrlMedico')

/*let _App = new App()
*/

let _Usuario = new Usuario()
let _Paciente = new Paciente()
let _Medico = new CtrlMedico()


//RUTAS USUARIOS
Router.post('/usuario/registrarse', _Usuario.crearCuenta)
//Router.get('/usuarios/info/:idPerfil', _Usuario.obtnerDatosUsuario)

Router.post('/usuarios/auth', _Usuario.iniciarSesion)
Router.post('/usuario/restorePassword', _Usuario.recuperarContrasena)

Router.put('/usuario/guardarContra', _Usuario.guardarContra)
Router.get('/usuarios/usuario/:idPerfil', _Usuario.getUser)
Router.post('/perfil/actualizar/:idPerfil', _Usuario.editarPerfil)
Router.post('/subir-foto', _Usuario.subirFoto)

//Expediente Pacientes
Router.get('/paciente/obtenerDatos/:nss', _Paciente.obtenerDatos)
Router.post('/medico/crearExpediente', _Medico.crearExpediente)
Router.get('/medico/obtenerExpediente/:nss', _Medico.obtenerExpediente)
Router.post('/medico/actualizarInformacion/:nss', _Medico.actualizarInfoPaciente)
Router.delete('/medico/eliminarExpediente/:nss', _Medico.eliminarExpediente)
Router.post('/nueva-alergia', _Paciente.agregarAlergia)
Router.put('/modificar-alergia', _Paciente.modificarAlergia)
Router.delete('/eliminar-alergia/:id', _Paciente.eliminarAlergia)
Router.get('/alergias/:nss', _Paciente.obtenerAlergias)

Router.post('/medico/registro', _Medico.crearCuenta)


module.exports = Router;