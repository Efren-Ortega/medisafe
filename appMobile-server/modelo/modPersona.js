class Persona{
    constructor(idPersona, name, segundo_nombre, apellido_p, apellido_m, fechaNac, edad, direccion, telefono){
        this.idPersona = idPersona;
        this.name = name;
        this.segundo_nombre = segundo_nombre;
        this.apellido_p = apellido_p;
        this.apellido_m = apellido_m;
        this.fechaNac = fechaNac;
        this.edad = edad;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    set setName(name){
        this.name = name
    }

    set setSegundoNombre(segundo_nombre){
        this.segundo_nombre = segundo_nombre
    }

    set setApellidoP(apellido_p){
        this.apellido_p = apellido_p
    }

    set setApellidoM(apellido_m){
        this.apellido_m = apellido_m
    }

    set setFechaNac(fechaNac){
        this.fechaNac = fechaNac
    }

    set setEdad(edad){
        this.edad = edad
    }

    set setDireccion(direccion){
        this.direccion = direccion
    }

    set setTelefono(telefono){
        this.telefono = telefono
    }
}

module.exports = Persona;