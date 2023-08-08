const Alergias = require("../modelo/Alergias")
const ModPaciente = require("../modelo/modPaciente")

class Paciente {


    obtenerDatos(req, res){
      let nss = req.params.nss
      let _model = new ModPaciente()
      _model.setNSS = nss
  
      _model.obtenerDatos()
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

    obtenerAlergias(req, res){
      let {nss} = req.params

      let _Paciente = new ModPaciente()
      _Paciente.setNSS = nss;
      let _model = new Alergias(null, null)
      _model.setPaciente = _Paciente
  
      _model.obtener()
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

    agregarAlergia(req, res){
      let {alergia, nss} = req.body

      console.log(alergia, nss)

      let _Paciente = new ModPaciente()
      _Paciente.setNSS = nss;
      let _model = new Alergias(null, alergia)
      _model.setPaciente = _Paciente
  
      _model.agregar()
        .then(row => {
          res.send({
            status: true,
            msg:row
          })
        })
        .catch(err => {
          console.log(err)
          res.send({
            msg: "Error al Guardar la Alergia",
            status: false,
            err:err
          })
        })
    }
  
    modificarAlergia(req, res){
      let {alergia, id} = req.body

      let _model = new Alergias(id, alergia)
  
      _model.modificar()
        .then(row => {
          res.send({
            status: true,
          })
        })
        .catch(err => {
          console.log(err)
          res.send({
            msg: "Error al Modificar la Alergia",
            status: false,
            err:err
          })
        })
    }

    eliminarAlergia(req, res){
      let {id} = req.params

      let _model = new Alergias(id, null)
  
      _model.eliminar()
        .then(row => {
          res.send({
            status: true,
          })
        })
        .catch(err => {
          console.log(err)
          res.send({
            msg: "Error al Eliminar la Alergia",
            status: false,
            err:err
          })
        })
    }

}
  
  
module.exports = Paciente