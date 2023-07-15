const {ModUsuarios, Pacientes, Medicos} = require('../modelo/modUsuarios')
const modelPerfil = require('../modelo/modPerfil')
/*const modelComentarios = require('../modelo/modComentarios')
const modeloPuntuacion = require('../modelo/modPuntacion')*/

const nodemailer = require('nodemailer');
const smtpTransport = require('nodemailer-smtp-transport');

class Usuario {


  crearCuenta(req, res) {

    let {rol} = req.body;
    let _model
    if(rol == "paciente"){
      let { name, last, middle, email, birth, age, direction, celphone, nss, pass} = req.body
      _model = new Pacientes(name, last, middle, email, birth, age, direction, celphone, rol)
      _model._setPass = pass;
      _model._setNss = nss;    
    
    }else if(rol === "medico"){
      let { name, last, middle, email, birth, age, direction, celphone, cedula, pass} = req.body
      _model = new Medicos(name, last, middle, email, birth, age, direction, celphone, rol)
      _model._setPass = pass;
      _model._setCedula = cedula;
    }
    

    //SI CUENTA EXISTE ENTONCES LA OPERACIÓN SE CANCELA
    _model.encontrarCuenta()
      .then(row => {

        //CREO LA CUENTA
        _model.crearCuenta()
          .then(row => {
            res.send({
              auth: row["token"],
              idPerfil:row["rows"]["insertId"],
              status: true
            })
          })
          .catch(err => {
            console.log(err)
            res.send({
              status: false
            })
          })

      })
      .catch(err => {
        res.send({
          error: "El correo ingresado ya se encuentra registrado",
          status: false
        })
      })

  }

  recuperarContrasena(req, res) {
    let {email, code} = req.body
    let _model = new ModUsuarios()
    _model.setEmail = email;

    _model.recuperarConstrasena(code)
    .then(info=>{
      res.send({
        status : true,
        info : info
      })
    })
    .catch(err=>{
      res.send({
        status:false,
        error:err
      })
    })    

  }

  editarPerfil(req, res) {

    if(!req.body.nombre){

      let _model = new modelPerfil(req.params.idPerfil)
      _model.setFoto = req.body.url_fotos
  
      _model.editarPerfil()
        .then(row => {
          res.send({
            msg: "Perfil Actualizado",
            status: true
          })
        })
        .catch(err => {
          console.log(err)
          res.send({
            msg: "Error al Actualizar el Pefil",
            status: false
          })
        })

    }else{

      let {nameImg, nombre, apellidos, correo, contra} = req.body
      
      console.log(nameImg, nombre, apellidos, correo, contra)

      let _modelUsuario = new Pacientes()
      _modelUsuario.setFoto = nameImg;
      _modelUsuario.nombre = nombre
      _modelUsuario.setEmail = correo
      _modelUsuario.setPass = contra
      _modelUsuario.apellido_p = apellidos

      let {idPerfil} = req.params

      console.log(idPerfil, "===", req.params)
  
      _modelUsuario.editarUsuario(idPerfil)
        .then(row => {
          res.send({
            msg: "Perfil Actualizado",
            status: true
          })
        })
        .catch(err => {
          console.log(err)
          res.send({
            msg: "Error al Actualizar el Pefil",
            status: false
          })
        })

    }


  }


  iniciarSesion(req, res) {

    let { pass, correo } = req.body
    let _model = new ModUsuarios()
    _model.setEmail = correo
    _model.setPass = pass

    _model.iniciarSesion()
      .then(row => {
        res.send({
          msg: "Usuario Autenticado",
          auth: row["token"],
          idPerfil: row["row"][0]["idprofile"],
          rol: row["row"][0]["rol"],
          status: true,
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Autenticar el Usuario",
          status: false
        })
      })

  }

  guardarContra(req, res){
    
    let { pass, email } = req.body
    let _model = new ModUsuarios()
    _model.setPass = pass
    _model.setEmail = email

    _model.guardarContra()
      .then(row => {
        res.send({
          msg: "Contraseña Guardada",
          status: true,
        })
      })
      .catch(err => {
        res.send({
          msg: "Error al Guardar la Contraseña",
          status: false,
          err:err
        })
      })
  }

  getUser(req, res){
    let idPerfil = req.params.idPerfil
    let _model = new ModUsuarios()

    _model.obtenerInfo(idPerfil)
      .then(row => {
        res.send({
          res: row,
          status: true,
        })
      })
      .catch(err => {
        res.send({
          msg: "Error al Obtener el Usuario",
          status: false,
          err:err
        })
      })
  }

  subirFoto(req, res){
    let { url_fotos } = req.body
    let _model = new ModUsuarios()

    _model.subirFoto(url_fotos)
      .then(row => {
        res.send({
          msg: "Foto Subida Guardada",
          status: true,
          data:row
        })
      })
      .catch(err => {
        res.send({
          msg: "Error al Subir la Foto",
          status: false,
          err:err
        })
      })
  }

  actualizarUsuario(req, res){
    let _model = new ModUsuarios()
    let {nameImg, nombre, correo, contra, num} = req.body
    _model.setFoto = req.body.url_fotos

    _model.editarPerfil()
      .then(row => {
        res.send({
          msg: "Perfil Actualizado",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Actualizar el Pefil",
          status: false
        })
      })
  }

}


module.exports = Usuario

