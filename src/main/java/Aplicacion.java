import beans.Eps;
import beans.Localidad;
import com.azoth.eve.Dao;
import com.azoth.eve.Parametro;
import com.azoth.eve.anotaciones.CampoTabla;
import com.azoth.eve.anotaciones.LlavePrimariaCompuesta;
import com.azoth.eve.condicionales.Condicional;
import com.azoth.eve.condicionales.Operacion;
import com.azoth.eve.conexion.GeneradorConexiones;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/*
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

*/
public abstract class Aplicacion {

    public static void main(String[] args) throws Exception {
        Class clasex = Class.forName("beans.Eps");

        System.out.println(clasex.getDeclaredAnnotation(LlavePrimariaCompuesta.class) != null);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream in = (classLoader.getResourceAsStream("conexion-bd.json"))){
            GeneradorConexiones generadorConexiones = GeneradorConexiones.getInstance();
            generadorConexiones.cargarParametros(in);
            try(Connection connection = generadorConexiones.obtenerConexion()){
                Dao dao = new Dao(connection,Eps.class);
                Dao dao2 = new Dao(connection,Localidad.class);

                Eps registro = (Eps)dao.obtenerRegistro(Arrays.asList(1));
                Localidad localidad = (Localidad) dao2.obtenerRegistro(Arrays.asList(1,1));



                CampoTabla campoTabla = clasex.getDeclaredField("codigo").getAnnotation(CampoTabla.class);
                Parametro parametro = new Parametro(
                    campoTabla,
                    Condicional.AND,
                    Operacion.MAYOR_IGUAL,
                    30
                );

                List<Object> registros = dao.listarRegistros(Arrays.asList(parametro));
                System.out.println();
                System.out.println(registro.toString());
                System.out.println();
                System.out.println(localidad.toString());

                for(Object eps : registros){
                    System.out.println();
                    System.out.println(((Eps)eps).toString());

                }
            }

            generadorConexiones.cerrarPoolDeConexiones();
        }
        catch (IOException | SQLException e){
            e.printStackTrace(System.err);
        }


        /*
        Eps eps = new Eps();

        //Class clase = Class.forName("beans.Eps");
        Class clase = Eps.class;
        Annotation annotation = clase.getAnnotation(NombreTabla.class);
        NombreTabla nombreTabla = (NombreTabla) clase.getAnnotation(NombreTabla.class);
        System.out.println(annotation.toString());
        System.out.println(nombreTabla.nombre());

        System.out.println();
        Annotation[] anotaciones = clase.getDeclaredAnnotations();
        for(Annotation anotacion : anotaciones)
            System.out.println(anotacion.toString());
        System.out.println();

        System.out.println();
        Annotation[] llaves = clase.getAnnotationsByType(LlavePrimaria.class);
        for(Annotation anotacion : llaves)
            System.out.println(anotacion.toString());
        System.out.println();

        for(Field f : clase.getDeclaredFields()){
            System.out.println("***" + f.getName()+ "***");
            CampoTabla campo = f.getAnnotation(CampoTabla.class);
            LlavePrimaria llavePrimaria = f.getAnnotation(LlavePrimaria.class);
            System.out.print(String.format("%s:%d:%s",campo.campo(),campo.tipoDato(),String.valueOf(llavePrimaria != null)));

            String nombre = f.getName();
            nombre = nombre.substring(0,1).toUpperCase() + nombre.substring(1);
            System.out.println();
            Method getter = clase.getDeclaredMethod("get"+nombre);
            System.out.print(nombre+"::"+getter.invoke(eps));

            System.out.println();
            System.out.println();


        }
        Object bean = clase.getDeclaredConstructor().newInstance();
        Method setter = clase.getDeclaredMethod("setCodigo", int.class);
        Object x = 1;
        setter.invoke(bean,x);

        Eps bean2 = (Eps) bean;
        System.out.println(bean2.toString());

        /*
        Dao dao = new Dao(null,Eps.class);
        Field codigo = clase.getDeclaredField("codigo");

        dao.obtenerRegistro("1");
        */

    }

}
