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
  
}
  
  
module.exports = Paciente