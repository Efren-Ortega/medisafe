//JWT (JSON Web Tokens) permite la creación de tokens de seguridad para trasmitir información encriptada entre el servidor y cliente consta de 3 partes:
//1.- cabecera -> Generalmente es Bearer que es usado para trasmitir tokens de autenticación
//2.- datos(payload)
//3.- firma digital -> Nos permitira verificar la autenticidad del token.


const connection = require("./conexion");

const jwt = require('jsonwebtoken');//Libraría para usar JWT
require('dotenv').config();//Uso variables de entorno la cual tiene mi firma digital para aplicar JWT

const nodemailer = require('nodemailer');
const smtpTransport = require('nodemailer-smtp-transport');

class ModUsuarios {

  constructor(name, last, middle, email, birth, age, direction, celphone) {
    this.nombre = name;
    this.apellido_p = last;
    this.apellido_m = middle;
    this.fechaNac = birth;
    this.edad = age;
    this.direccion = direction;
    this.telefono = celphone;
    this.email = email;
    this.pass = null;
    this.url_foto = null;
    this.con = connection;
  }


  set setEmail(email){
    this.email = email
  }

  set setPass(pass){
    this.pass = pass
  }


  
  obtenerInfo(idPerfil) {
    return new Promise((resolve, reject) => {      
      connection.query(`SELECT *FROM usuarios
      INNER JOIN profile ON profile.id_user = usuarios.idusuario
      INNER JOIN persona ON persona.idpersona = profile.id_persona
      WHERE profile.idprofile=${idPerfil}`, (err, rows) => {
        console.log(err)
        if (err || rows.length == 0) return reject(err)
        return resolve(rows)
      })
    })
  }


  crearCuenta() {

    return new Promise((resolve, reject) => {

      //CREO A UN USUARIO
      connection.query(`INSERT INTO usuario(nombre, apellido_p, apellido_m, pass, correo) 
      VALUES('${this.nombre}', '${this.apellido_p}', '${this.apellido_m}', '${this.pass}', '${this.correo}')`, (err, rows) => {
        if (err) return reject(err)

        //UNA VEZ CREADO EL USUARIO CREO SU PERFIL TOMANDO EL ID DEL USUARIO CREADO
        connection.query(`INSERT INTO perfil(idUsuario, usuario, url_foto, pais) VALUES ('${rows.insertId}', '${this.usuario}', 'user_default.png', null)`, (err, rows) => {
          if (err) return reject(err)

          return resolve(rows)
        })


      })
    })

  }

  //VERIFICO SI YA HAY USUARIOS REGISTRADOS POR SU CORREO
  encontrarCuenta() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *FROM usuarios WHERE correo = '${this.email}'`, (err, rows) => {
        if (err || rows.length > 0) return reject(err)
        return resolve(rows)
      })
    })
  }

  recuperarCuenta() {

  }

  editarPerfil(id) {

    return new Promise((resolve, reject) => {
      connection.query(`UPDATE perfil SET usuario='${this.usuario}', url_foto='${this.url_foto}', pais='${this.pais}' WHERE idPerfil = ${id}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })

  }

  iniciarSesion() {
    return new Promise((resolve, reject) => {


      connection.query(`SELECT *FROM usuarios
      INNER JOIN profile ON profile.id_user = usuarios.idusuario
      INNER JOIN persona ON persona.idpersona = profile.id_persona
      WHERE usuarios.correo = '${this.email}' AND usuarios.contrasena = '${this.pass}'`, (err, row) => {

        if (err || row.length == 0) return reject(err)

        //MI FIRMA DIGITAL
        const SECRET_KEY = process.env.SECRET_KEY;

        //LOS DATOS A TRASMITIR AL CLIENTE
        const payload = {
          idperfil: row[0]['idprofile'],
        };

        //OPCIONES EXTRAS
        const options = {
          expiresIn: '24h'//LA SESIÓN EXPIRARA EN 24H
        };

        //GENERAMOS EL TOKEN CON CON LOS DATOS Y LA FIRMA DIGITAL
        const token = jwt.sign(payload, SECRET_KEY, options);

        //ENVIO EL TOKEN AL CLIENTE; ESTE TOKEN DEBERA SER ALMACENADO EN EL CLIENTE LOCALMENTE PAR FUTURA PETICIONES
        return resolve({ token, row })
      })
    })
  }

  guardarContra(){
    return new Promise((resolve, reject) => {

      console.log(this.email)
      console.log(this.pass)

      connection.query(`UPDATE usuarios SET contrasena='${this.pass}' WHERE correo = '${this.email}'`, (err, row) => {
        if (err) return reject(err)
        if(row['affectedRows'] == 0){
          return reject("Correo no Existe")
        }

        return resolve(row)
      })
      
    })
  }

  recuperarConstrasena(codigo){
    // Configuración del transporte de correo
    const transporter = nodemailer.createTransport(smtpTransport({
      service: 'gmail',
      auth: {
        user: 'efrenortega@micorreo.upp.edu.mx',
        pass: process.env.PASSWORD
      }
    }));
    
    // Configuración del correo electrónico
    const mailOptions = {
      from: 'efrenortega@micorreo.upp.edu.mx',
      to: `${this.email}`,
      subject: 'Código de Recuperación de Contraseña',
      html: `
      <html>
      <body style="font-family: Roboto, sans-serif; background-color: #f2f2f2;">
        <div style="max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);">
          <h1 style="color: #333; text-align: center;">Recuperación de contraseña</h1>
          <p style="color: #666; line-height: 1.5;">Hola, Somos MediSafe</p>
          <p style="color: #666; line-height: 1.5;">Recibimos una solicitud para restablecer tu contraseña. A continuación, encontrarás un código de verificación:</p>
          <div style="text-align: center; font-size: 24px; margin-top: 30px; margin-bottom: 40px; padding: 10px; background-color: #f9f9f9; border: 1px solid #ddd; border-radius: 3px;">
            ${codigo}
          </div>
          <p style="color: #666; line-height: 1.5;">Ingresa este código de recuperación en la app para continuar.</p>
          <p style="color: #666; line-height: 1.5;">Si no solicitaste esta acción, puedes ignorar este correo.</p>
          <p style="color: #666; line-height: 1.5;">¡Gracias!</p>
        </div>
      </body>
    </html>
    `
    };
    

    return new Promise((resolve, reject)=>{
      transporter.sendMail(mailOptions, function(error, info){
        if (error) {
          reject(error)
        } else {
          resolve(info.response)
        }
      });
    })

    
  }

}

class Pacientes extends ModUsuarios{

  constructor(name, last, middle, email, birth, age, direction, celphone, rol){
    super(name, last, middle, email, birth, age, direction, celphone)
    this._nss = null;
    this._pass = null;
    this.rol = rol;
  }

  set _setPass(pass){
    this._pass = pass;
  }

  set _setNss(nss){
    this._nss = nss;
  }

  crearCuenta() {

    return new Promise((resolve, reject) => {

      //CREO A UN USUARIO
      connection.query(`INSERT INTO usuarios(usuario, correo, contrasena, rol) 
      VALUES('OMITOD TEMPORAL', '${this.email}', '${this._pass}', '${this.rol}')`, (err, rows) => {
        if (err) return reject(err)

        //Create a person with the data
        connection.query(`INSERT INTO persona(name, apellido_p, apellido_m, fechaNac, edad, direccion, telefono)
          VALUES ('${this.nombre}', '${this.apellido_p}', '${this.apellido_m}', ${null},  ${null},  ${null},  ${null})`, (err, rowPerson)=>{
            if(err) return reject(err)


            //Create the prfile with the insert id of users and person above
            connection.query(`INSERT INTO profile(id_user, id_persona, celula_nss, photo) VALUES (${rows.insertId}, ${rowPerson.insertId}, '${this._nss}', 'paciente.jpg')`, (err, rows) => {
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

class Medicos extends ModUsuarios{
  constructor(name, last, middle, email, birth, age, direction, celphone, rol){
    super(name, last, middle, email, birth, age, direction, celphone)
    this._cedula = null;
    this._pass = null;
    this.rol = rol;
  }

  set _setPass(pass){
    this._pass = pass;
  }

  set _setCedula(cedula){
    this._cedula = cedula;
  }

  crearCuenta() {

    return new Promise((resolve, reject) => {

      //CREO A UN USUARIO
      connection.query(`INSERT INTO usuarios(usuario, correo, contrasena, rol) 
      VALUES('OMITOD TEMPORAL', '${this.email}', '${this._pass}', '${this.rol}')`, (err, rows) => {
        if (err) return reject(err)

        //Create a person with the data
        connection.query(`INSERT INTO persona(name, apellido_p, apellido_m, fechaNac, edad, direccion, telefono)
          VALUES ('${this.nombre}', '${this.apellido_p}', '${this.apellido_m}', ${null},  ${null},  ${null},  ${null})`, (err, rowPerson)=>{
            if(err) return reject(err)


            //Create the prfile with the insert id of users and person above
            connection.query(`INSERT INTO profile(id_user, id_persona, celula_nss, photo) VALUES (${rows.insertId}, ${rowPerson.insertId}, '${this._cedula}', 'medico.jpg')`, (err, rows) => {
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

module.exports = {ModUsuarios, Pacientes, Medicos};