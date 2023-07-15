const connection = require("./conexion");
let fs = require('fs');
const path = require('path');
const { v4: uuidv4 } = require('uuid');

class ModPerfil {
  constructor(id) {
    this.idPerfil = id
    this.usuario = null;
    this.url_foto = null;
    this.pais = null;
  }

  set setUsuario(usuario) {
    this.usuario = usuario;
  }

  set setFoto(url_foto) {
    this.url_foto = url_foto
  }

  set setPais(pais) {
    this.pais = pais
  }

  editarPerfil() {
    return new Promise((resolve, reject) => {
      connection.query(`UPDATE profile SET photo='${this.url_foto}' WHERE idprofile = ${this.idPerfil}`, (err, row) => {
        if (err) return reject(err)
        
        return resolve(row)
      })

    })

  }

  subirFoto(base64) {

    const base64Image = base64.split(';base64,').pop();
    const filename = `${uuidv4()}.jpg`;

    const filePath = path.join(__dirname, '../imagenes', filename);

    return new Promise((resolve, reject) => {
      fs.writeFile(filePath, base64Image, { encoding: 'base64' }, (error) => {
        if (error) {
          console.error('Error al guardar la imagen:', error);
          return reject(error)

        } else {
          return resolve(filename)
        }
      });
    });

  }

}

module.exports = ModPerfil;