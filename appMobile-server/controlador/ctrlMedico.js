const Medico = require("../modelo/modMedicos");
const Persona = require("../modelo/modPersona");
const { Pacientes } = require("../modelo/modUsuarios");


class CtrlMedico{

    crearExpediente(req, res) {
        let _model
        
        let {name, edad, middleName, last1, last2, nss} = req.body

        const objPersona = new Persona();
        objPersona.setName = name
        objPersona.setSegundoNombre = middleName
        objPersona.setApellidoP = last1
        objPersona.setApellidoM = last2
        objPersona.setEdad = edad

        const objPaciente = new Pacientes()
        objPaciente._setNss = nss;

        _model = new Medico()   
        _model.setPersona = objPersona
        _model.setPaciente = objPaciente        
    
        //SI CUENTA EXISTE ENTONCES LA OPERACIÓN SE CANCELA
        _model.crearExpediente()
          .then(row => {
    
            res.send({
                data:row["rows"],
                status: true
            })
    
          })
          .catch(err => {
            console.log(err)
            res.send({
              error: "Error al crear el Expediente",
              status: false
            })
          })
    }

    obtenerExpediente(req, res){

        let nss = req.params.nss
        const objPaciente = new Pacientes()
        objPaciente._setNss = nss

        const _model = new Medico()   
        _model.setPaciente = objPaciente
    
        _model.obtenerExpediente()
          .then(row => {
            res.send({
              res: row,
              status: true,
            })
          })
          .catch(err => {
            res.send({
              msg: "Error al Obtener el Expediente",
              status: false,
              err:err
            })
          })
    }

    actualizarInfoPaciente(req, res) {

      let {nombre, fechaNac, direccion, telefono, sexo, correo} = req.body
  
        let {nss} = req.params
      
        const objPersona = new Persona()
        objPersona.setName = nombre
        objPersona.setFechaNac = fechaNac
        objPersona.setDireccion = direccion
        objPersona.setTelefono = telefono

        const objPaciente = new Pacientes()
        objPaciente._setNss = nss;

        const _model = new Medico()      
        _model.setPersona = objPersona
        _model.setPaciente = objPaciente  
        
        _model.editarExpediente()
          .then(row => {
            res.send({
              msg: "Información Actualizada",
              status: true
            })
          })
          .catch(err => {
            console.log(err)
            res.send({
              msg: "Error al Actualizar la Información",
              status: false
            })
          })
  
    }

    eliminarExpediente(req, res) {
  
        let {nss} = req.params

        const objPaciente = new Pacientes()
        objPaciente._setNss = nss;

        const _model = new Medico()      
        _model.setPaciente = objPaciente  
        
        _model.eliminarExpediente()
          .then(row => {
            res.send({
              msg: "Expediente Eliminado",
              status: true
            })
          })
          .catch(err => {
            console.log(err)
            res.send({
              msg: "Error al Eliminar el Expediente",
              status: false
            })
          })
  
    }

    crearCuenta(req, res) {
      let _model
      
      let {name, last1, last2, cedula, correo, pass} = req.body
      console.log(name)
      const objPersona = new Persona();
      objPersona.setName = name
      objPersona.setApellidoP = last1
      objPersona.setApellidoM = last2

      _model = new Medico()   
      _model.setPersona = objPersona
      _model.cedula = cedula  
      _model.email = correo
      _model.pass = pass
  
      //SI CUENTA EXISTE ENTONCES LA OPERACIÓN SE CANCELA
      _model.crearCuenta()
        .then(row => {
  
          res.send({
              data:row["rows"],
              auth:row['token'],
              idPerfil:row['rows']['insertId'],

              status: true
          })
  
        })
        .catch(err => {
          console.log(err)
          res.send({
            error: "Error al crear la cuenta",
            status: false
          })
        })
  }
}


module.exports = CtrlMedico