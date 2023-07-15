const connection = require("./conexion")
const { ModUsuarios } = require("./modUsuarios")

const jwt = require('jsonwebtoken');//Libraría para usar JWT
require('dotenv').config();//Uso variables de entorno la cual tiene mi firma digital para aplicar JWT

class Medico extends ModUsuarios{    

    constructor(Persona, Paciente, cedula, email, pass){
        super(email, pass)
        this.Persona = Persona
        this.Paciente = Paciente
        this.cedula = cedula
        this.idEspecializacion = null
        this.idHospitalClinica = null
    }

    set setPersona(Persona){
        this.Persona = Persona
    }

    set setPaciente(Paciente){
        this.Paciente = Paciente
    }

    crearCuenta() {
        return new Promise((resolve, reject) => {

            connection.query(`SELECT *FROM usuarios WHERE correo = '${this.email}'`, (err, rowUsuario)=>{
                if(err || rowUsuario.length != 0) return reject(err)

                //CREO A UN USUARIO
          connection.query(`INSERT INTO usuarios(usuario, correo, contrasena, rol) 
          VALUES('OMITOD TEMPORAL', '${this.email}', '${this.pass}', 'medico')`, (err, rows) => {
            if (err) return reject(err)
    
                //Create a person with the data
                connection.query(`INSERT INTO persona(name, apellido_p, apellido_m, fechaNac, edad, direccion, telefono)
                VALUES ('${this.Persona.name}', '${this.Persona.apellido_p}', '${this.Persona.apellido_m}', ${null},  ${null},  ${null},  ${null})`, (err, rowPerson)=>{
                    if(err) return reject(err)
        
        
                    //Create the prfile with the insert id of users and person above
                    connection.query(`INSERT INTO profile(id_user, id_persona, celula_nss, photo) VALUES (${rows.insertId}, ${rowPerson.insertId}, '${this.cedula}', 'medico.jpg')`, (err, rows) => {
                    if (err) return reject(err)
        
                    //MI FIRMA DIGITAL
                    const SECRET_KEY = process.env.SECRET_KEY;
        
                    //OPCIONES EXTRAS
                    const options = {
                        expiresIn: '24h'//LA SESIÓN EXPIRARA EN 24H
                    };
        
                    //GENERAMOS EL TOKEN CON CON LOS DATOS Y LA FIRMA DIGITAL
                    const token = jwt.sign({}, SECRET_KEY, options);
        
                    //ENVIO EL TOKEN AL CLIENTE; ESTE TOKEN DEBERA SER ALMACENADO EN EL CLIENTE LOCALMENTE PAR FUTURA PETICIONES
                    return resolve({ token, rows })
                    })
        
                })
            })
    
            })
    
          
        })
    }

    crearExpediente() {

        return new Promise((resolve, reject) => {
        connection.beginTransaction((err) => {
            if (err) {
                reject(err);
                return;
            }

            connection.query(`INSERT INTO usuarios(usuario, correo, nss, contrasena, rol) 
            VALUES('OMITOD TEMPORAL', 'NULL', '${this.Paciente._nss}', 'NULL', 'paciente')`, (err, rows) => {
            if (err) {
                connection.rollback(() => {
                    reject(err);
                });
                return;
            }

            connection.query(`INSERT INTO persona(name, segundo_nombre, apellido_p, apellido_m, fechaNac, edad, direccion, telefono)
                VALUES ('${this.Persona.name}', '${this.Persona.segundo_nombre}', '${this.Persona.apellido_p}', '${this.Persona.apellido_m}', ${null},  ${parseInt(this.Persona.edad)},  ${null},  ${null})`, (err, rowPerson) => {
                if (err) {
                connection.rollback(() => {
                    reject(err);
                });
                return;
                }

                connection.query(`INSERT INTO profile(id_user, id_persona, celula_nss, photo)
                VALUES (${rows.insertId}, ${rowPerson.insertId}, '${this.Paciente._nss}', 'paciente.jpg')`, (err, rows) => {
                if (err) {
                    connection.rollback(() => {
                    reject(err);
                    });
                    return;
                }

                connection.query(`INSERT INTO pacientes(NSS, tipo_sangre, id_persona)
                    VALUES ('${this.Paciente._nss}', 'NULL', ${rowPerson.insertId})`, (err, rowPaciente) => {
                    if (err) {
                    connection.rollback(() => {
                        reject(err);
                    });
                    return;
                    }

                    connection.commit((err) => {
                    if (err) {
                        connection.rollback(() => {
                        reject(err);
                        });
                        return;
                    }

                    resolve(rows);
                    });
                });
                });
            });
            });
        });
        });

    }

    obtenerExpediente(){
        console.log(this.Paciente._nss)
        return new Promise((resolve, reject) => {      
            connection.query(`SELECT *FROM pacientes 
            INNER JOIN persona ON pacientes.id_persona = persona.idpersona
            JOIN profile ON profile.id_persona = pacientes.id_persona 
            WHERE pacientes.NSS='${this.Paciente._nss}'`, (err, rows) => {
              console.log(err)
              if (err || rows.length == 0) return reject(err)
              return resolve(rows)
            })
          })
    }
    
    editarExpediente() {
        return new Promise((resolve, reject) => {
          connection.query(`UPDATE persona
          JOIN pacientes ON pacientes.id_persona = persona.idpersona
          SET persona.name = '${this.Persona.name}', persona.fechaNac = '${this.Persona.fechaNac}', 
          persona.direccion = '${this.Persona.direccion}', persona.telefono = '${this.Persona.telefono}'
          WHERE pacientes.NSS = '${this.Paciente._nss}';`, (err, row) => {
            if (err) return reject(err)
            
            return resolve(row)
          })
    
        })
    
    }

    eliminarExpediente() {
        return new Promise((resolve, reject) => {
          connection.query(`SELECT id_persona FROM pacientes WHERE NSS = '${this.Paciente._nss}'`, (err, rowPaciente) => {
            if (err || rowPaciente.length == 0) return reject(err)

            let idPersona = rowPaciente[0]['id_persona']

            console.log(rowPaciente)
            console.log(rowPaciente[0])

            connection.query(`DELETE usuarios, profile, pacientes
            FROM profile
            JOIN usuarios ON usuarios.nss = profile.celula_nss
            JOIN pacientes ON pacientes.NSS = usuarios.nss
            WHERE profile.celula_nss = '${this.Paciente._nss}'`, (err, row) => {
                if (err || row['affectedRows'] === 0) return reject(err)                

                connection.query(`DELETE FROM persona WHERE idpersona = ${parseInt(idPersona)}`, (err, rowPersona) => {
                    if (err || rowPersona['affectedRows'] === 0) return reject(err)
                    
                    return resolve(row)
                })
            })

          })
    
        })
    
    }

}

module.exports = Medico;