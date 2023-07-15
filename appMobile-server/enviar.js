const nodemailer = require('nodemailer');
const smtpTransport = require('nodemailer-smtp-transport');

function generarCodigoAleatorio() {
    let codigo = '';
    for (let i = 0; i < 6; i++) {
      const digito = Math.floor(Math.random() * 10);
      codigo += digito;
    }
    return codigo;
}

// Configuración del transporte de correo
const transporter = nodemailer.createTransport(smtpTransport({
  service: 'gmail',
  auth: {
    user: 'efrenortega@micorreo.upp.edu.mx',
    pass: 'UPDL54Wwvh'
  }
}));

// Configuración del correo electrónico
const mailOptions = {
  from: 'ojairsjs@gmail.com',
  to: 'efrensjs@outlook.com',
  subject: 'Código de Recuperación de Contraseña',
  html: `
  <html>
  <body style="font-family: Roboto, sans-serif; background-color: #f2f2f2;">
    <div style="max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);">
      <h1 style="color: #333; text-align: center;">Recuperación de contraseña</h1>
      <p style="color: #666; line-height: 1.5;">Hola, Somos MediSafe</p>
      <p style="color: #666; line-height: 1.5;">Recibimos una solicitud para restablecer tu contraseña. A continuación, encontrarás un código de verificación:</p>
      <div style="text-align: center; font-size: 24px; margin-top: 30px; margin-bottom: 40px; padding: 10px; background-color: #f9f9f9; border: 1px solid #ddd; border-radius: 3px;">
        ${generarCodigoAleatorio()}
      </div>
      <p style="color: #666; line-height: 1.5;">Ingresa este código de recuperación para continuar.</p>
      <p style="color: #666; line-height: 1.5;">Si no solicitaste esta acción, puedes ignorar este correo.</p>
      <p style="color: #666; line-height: 1.5;">¡Gracias!</p>
    </div>
  </body>
</html>
`
};

// Enviar el correo electrónico
transporter.sendMail(mailOptions, function(error, info){
  if (error) {
    console.log('Error al enviar el correo:', error);
  } else {
    console.log('Correo enviado:', info.response);
  }
});