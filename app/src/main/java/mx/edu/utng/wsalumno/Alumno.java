package mx.edu.utng.wsalumno;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Jorge on 29/03/2017.
 */
public class Alumno implements KvmSerializable {

    private int cveUniversidad;
    private String matricula;
    private String notas;
    private String fechaAlta;
    private boolean becado;
    private String grupo;
    private int generacion;
    private String nombre;

    public Alumno(int cveUniversidad, String matricula, String notas, String fechaAlta, boolean becado, String grupo, int generacion, String nombre) {
        this.cveUniversidad = cveUniversidad;
        this.matricula = matricula;
        this.notas = notas;
        this.fechaAlta = fechaAlta;
        this.becado = becado;
        this.grupo = grupo;
        this.generacion = generacion;
        this.nombre = nombre;
    }

    public Alumno() {
        this(0,"","","",true,"",0,"");
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return cveUniversidad;
            case 1:
                return matricula;
            case 2:
                return notas;
            case 3:
                return fechaAlta;
            case 4:
                return becado;
            case 5:
                return grupo;
            case 6:
                return generacion;
            case 7:
                return nombre;
        }

        return  null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                cveUniversidad =Integer.parseInt(o.toString());
                break;
            case 1:
                matricula = o.toString();
                break;
            case 2:
                notas = o.toString();
                break;
            case 3:
                fechaAlta = o.toString();
                break;
            case 4:
                becado = Boolean.parseBoolean(o.toString());
                break;
            case 5:
                grupo = o.toString();
                break;
            case 6:
                generacion= Integer.parseInt(o.toString());
                break;
            case 7:
                nombre = o.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "cveUniversidad";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "matricula";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "notas";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "fechaAlta";
                break;
            case 4:
                propertyInfo.type = Boolean.class;
                propertyInfo.name = "becado";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "grupo";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "generacion";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "nombre";
                break;
            default:
                break;
        }


    }


}
