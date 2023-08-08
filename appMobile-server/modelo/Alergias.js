const connection = require("./conexion")

class Alergias{
    constructor(id, alergia, gravedad, Paciente){
        this.id= id
        this.alergia = alergia
        this.gravedad = null
        this.Paciente = null
    }

    set setPaciente(Paciente){
        this.Paciente = Paciente
    }

    obtener(){
        return new Promise((resolve, reject) => {
            connection.query(`SELECT paciente_alergias.idpaciente_alergias, alergias.alergia FROM paciente_alergias 
            INNER JOIN alergias ON paciente_alergias.id_alergias = alergias.idalergias 
            WHERE paciente_alergias.id_paciente = '${this.Paciente.NSS}'`, (err, rows) => {
                if (err) return reject(err)
                
                return resolve(rows) 
            })
        })
    }

    agregar(){
        return new Promise((resolve, reject) => {
            //CREO A UN USUARIO
            connection.query(`SELECT * FROM paciente_alergias
            INNER JOIN alergias ON alergias.idalergias = paciente_alergias.id_alergias
            WHERE alergias.alergia = '${this.alergia}' AND paciente_alergias.id_paciente = '${this.Paciente.NSS}';`, (err, rows) => {
                if(err) return reject(err)

                if(rows.length == 0){
                    connection.query(`SELECT idalergias FROM alergias WHERE alergia = '${this.alergia}'`, (err, rows) => {
                        if (err) return reject(err)
          
                          let idAlergia = null;
          
                          if(rows.length > 0){
                              idAlergia = rows[0]['idalergias']
                          }
          
                              
          
                              if(rows.length == 0){
                                  connection.query(`INSERT INTO alergias(alergia, gravedad) 
                                  VALUES('${this.alergia}', 'NULL')`, (err, rows) => {
              
                                      if (err) return reject(err)
              
                                      const insertedId = rows.insertId;
                                      idAlergia = insertedId;
                                      console.log(idAlergia)
              
                                      connection.query(`INSERT INTO paciente_alergias(id_alergias, id_paciente) 
                                      VALUES('${insertedId}', '${this.Paciente.NSS}')`, (err, rows) => {
                                          if (err) return reject(err)
                                  
                                          return resolve(rows)
                                      })  
                                  })
                              }else{
                                  connection.query(`INSERT INTO paciente_alergias(id_alergias, id_paciente) 
                                  VALUES('${idAlergia}', '${this.Paciente.NSS}')`, (err, rows) => {
                                      if (err) return reject(err)
                              
                                      return resolve(rows)
                                  }) 
                              } 
                                 
          
          
                    })
                }else{
                    return resolve("Esa Alergia ya esta Registrada")
                }

            })

            
        })
    }

    eliminar(){
        return new Promise((resolve, reject) => {
            connection.query(`DELETE FROM paciente_alergias WHERE idpaciente_alergias = ${parseInt(this.id)}`, (err, rows) => {
                if (err) return reject(err)
                        
                return resolve(rows) 
            })
        })
    }

    modificar(){
        return new Promise((resolve, reject) => {
            //CREO A UN USUARIO
            connection.query(`SELECT idalergias FROM alergias WHERE alergia = '${this.alergia}'`, (err, rows) => {
              if (err) return reject(err)

                let idAlergia = null;

                if(rows.length > 0){
                    idAlergia = rows[0]['idalergias']
                }

                if(rows.length == 0){
                    connection.query(`INSERT INTO alergias(alergia, gravedad) 
                    VALUES('${this.alergia}', 'NULL')`, (err, rows) => {
                        if (err) return reject(err)

                        
                        const insertedId = rows.insertId;
                        idAlergia = insertedId;

                        connection.query(`UPDATE paciente_alergias SET id_alergias = ${parseInt(idAlergia)} WHERE idpaciente_alergias = ${parseInt(this.id)}`, (err, rows) => {

                            if (err) return reject(err)
                    
                            return resolve(rows)
                        })  
                    })
                }else{
                    connection.query(`UPDATE paciente_alergias SET id_alergias = ${parseInt(idAlergia)} WHERE idpaciente_alergias = ${parseInt(this.id)}`, (err, rows) => {
                        if (err) return reject(err)
                
                        return resolve(rows)
                    }) 
                }


                 
                            


            })
        })
    }

    

}

module.exports = Alergias;