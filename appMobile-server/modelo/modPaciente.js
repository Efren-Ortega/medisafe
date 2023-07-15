const connection = require("./conexion");
const Persona = require("./modPersona");

class ModPaciente extends Persona{
    constructor(Usuario, name, segundo_nombre, apellido_p, apellido_m, edad, NSS, tipo_Sangre, id_persona) {
        super(name, segundo_nombre, apellido_p, apellido_m, edad)
        this.Usuario = Usuario
        this.NSS = NSS
        this.tipo_Sangre = tipo_Sangre;
        this.id_persona = id_persona;
    }
  
    set setNSS(NSS) {
      this.NSS = NSS;
    }
  
    set setTipoSangre(tipoSangre) {
        this.tipo_Sangre = tipoSangre;
    }

    set setIdPersona(idPersona) {
        this.id_persona = idPersona;
    }
  
    obtenerDatos(){
        return new Promise((resolve, reject) => {      
            connection.query(`SELECT *FROM pacientes 
            INNER JOIN persona ON pacientes.id_persona = persona.idpersona
            JOIN profile ON profile.id_persona = pacientes.id_persona 
            WHERE pacientes.NSS='${this.NSS}'`, (err, rows) => {
              console.log(err)
              if (err || rows.length == 0) return reject(err)
              return resolve(rows)
            })
          })
    }

    

    crearCuenta() {

        return new Promise((resolve, reject) => {
    
          //CREO A UN USUARIO
          connection.query(`INSERT INTO usuarios(usuario, correo, contrasena, rol) 
          VALUES('OMITOD TEMPORAL', '${this.Usuario.email}', '${this.Usuario._pass}', 'paciente')`, (err, rows) => {
            if (err) return reject(err)
    
            //Create a person with the data
            connection.query(`INSERT INTO persona(name, apellido_p, apellido_m, fechaNac, edad, direccion, telefono)
              VALUES ('${this.name}', '${this.apellido_p}', '${this.apellido_m}', ${null},  ${null},  ${null},  ${null})`, (err, rowPerson)=>{
                if(err) return reject(err)
    
    
                //Create the prfile with the insert id of users and person above
                connection.query(`INSERT INTO profile(id_user, id_persona, celula_nss, photo) VALUES (${rows.insertId}, ${rowPerson.insertId}, '${this.NSS}', 'paciente.jpg')`, (err, rows) => {
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
    
    }
  
}
  
module.exports = ModPaciente;