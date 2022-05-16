package com.example.quality_control;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Conexion
{
    private java.sql.Connection connection = null;
    private final String url = "jdbc:microsoft:sqlserver://";
    private final String serverName = "184.168.47.21";
    private final String portNumber = "1433";
    private final String databaseName = "prod_001";
    private final String userName = "C113nt3";
    private final String password = "rio2014$$";
    // Informs the driver to use server a side-cursor,
    // which permits more than one active statement
    // on a connection.
    private final String selectMethod = "Direct";


    // Constructor
    public Conexion()
    {

    }

    public boolean verificaConexion(Context ctx)
    {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED)
            {
                bConectado = true;
            }
        }
        return bConectado;
    }



    //PROCEDIMIENTOS QUE ME DEVUELVE LOS DATOS DE UN USUARIO CONSULTADO
    public Usuario ConsultarUsuario (String usuario, String clave)
    {
        Usuario re_usuario = new Usuario();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("exec [consultar].[PA_Consultar_Usuario] \'"+usuario +"\',\'"+clave+"\';");

                while (result.next())
                {
                    re_usuario.setCodigo(result.getLong(1));
                    re_usuario.setNombre(result.getString(2));
                    re_usuario.setNombreLog(result.getString(3));
                    re_usuario.setContrasena(result.getString(4));
                    re_usuario.setTelefono(result.getString(5));
                    re_usuario.setCorreo(result.getString(6));
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return re_usuario;
    }



    public List<Plantas> ConsultarPlantas ()
    {
        List<Plantas> Listado_Plantas = new ArrayList<Plantas>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("exec [consultar].[PA_ConsultarSoloProveedores];");

                while (result.next())
                {
                    Plantas re_planta = new Plantas();
                    re_planta.setCodigo(result.getLong(1));
                    re_planta.setNombre(result.getString(2));
                    re_planta.setTelefono(result.getString(3));
                    re_planta.setCorreo(result.getString(4));
                    Listado_Plantas.add(re_planta);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_Plantas;
    }

    public List<String> ConsultarLotes (long cod_empresa, long numero)
    {
        List<String> Listado_Lotes = new ArrayList<String>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("select codigo_lote from Lotes where empresa = " + cod_empresa + "and numero = " + numero);

                while (result.next())
                {
                    String codigo = new String();
                    codigo = result.getString(1);
                    Listado_Lotes.add(codigo);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_Lotes;
    }

    public List<Tipo> ConsultarTipo ()
    {
        List<Tipo> Listado_Tipo = new ArrayList<Tipo>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("select distinct upper(descripcion),codigo from refr.Tipo order by 1");

                while (result.next())
                {
                    Tipo re_tipo = new Tipo();

                    re_tipo.setNombre(result.getString(1));
                    re_tipo.setCodigo(result.getLong(2));
                    //500 es el codigo del Add new Item para que no aparezca en las lista marca
                    if(re_tipo.getCodigo()!=500)
                        Listado_Tipo.add(re_tipo);

                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_Tipo;
    }

    //select codigo, upper(descripcion) from mastr.Marca where empresa = 40130514
    public List<Marca> ConsultarMarca ()
    {
        List<Marca> Listado_Marca = new ArrayList<Marca>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("select distinct upper(descripcion),codigo,empresa from mastr.Marca where codigo <> 100 and descripcion <> '' order by 1 ");

                while (result.next())
                {
                    Marca re_marca = new Marca();
                    re_marca.setNombre(result.getString(1));
                    re_marca.setCodigo(result.getLong(2));
                    re_marca.setEmpresa(result.getLong(3));
                    Listado_Marca.add(re_marca);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_Marca;
    }


    //select codigo, upper(descripcion) from mastr.Marca where empresa = 40130514
    public List<Talla> ConsultarTalla ()
    {
        List<Talla> Listado_Talla = new ArrayList<Talla>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("select distinct upper(descripcion),codigo,tipo from refr.Talla order by 1 ");

                while (result.next())
                {
                    Talla re_talla = new Talla();
                    re_talla.setNombre(result.getString(1));
                    re_talla.setCodigo(result.getLong(2));
                    re_talla.setTipo(result.getInt(3));
                    Listado_Talla.add(re_talla);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_Talla;
    }

    public List<Packing> ConsultarEmpaque ()
    {
        List<Packing> Listado_Empaque = new ArrayList<Packing>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("select codigo,(case when unidadMedida = 1 Then descripcion + ' Lbs.' else descripcion + ' Kgs.' end) as Packing from refr.Packing where codigo <> 500 and codigo <> 501");

                while (result.next())
                {
                    Packing re_empaque = new Packing();
                    re_empaque.setCodigo(result.getLong(1));
                    re_empaque.setDescripcion(result.getString(2));
                    Listado_Empaque.add(re_empaque);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_Empaque;
    }


    public List<Cabecera_Orden> ConsultarOrdenes ()
    {
        List<Cabecera_Orden> Listado_cabecera = new ArrayList<Cabecera_Orden>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("exec [android].[PA_Consultar_PurchaseOrderGEN_Calidad];");

                while (result.next())
                {
                    Cabecera_Orden orden = new Cabecera_Orden();
                    orden.setEmpresa(result.getLong(1));
                    orden.setNombre_Empresa(result.getString(2));
                    orden.setNumero_TB(result.getLong(3));
                    orden.setHarvest(result.getString(7));
                    orden.setEstado(result.getString(5));
                    orden.setNombre_Proveedor(result.getString(8));
                    orden.setFechaETD(result.getString(9));
                    orden.setFechaETA(result.getString(10));
                    orden.setTotal(result.getString(15));
                    orden.setProveedor(result.getLong(16));
                    orden.setProcesado(false);
                    Listado_cabecera.add(orden);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_cabecera;
    }


    public List<DetalleOrder> ConsultarDetalleOrdenes (long cod_empresa, long numero)
    {
        List<DetalleOrder> Listado_detalle = new ArrayList<DetalleOrder>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("exec [android].[PA_Consultar_DetalleOrder] " + cod_empresa + "," + numero +";");

                while (result.next())
                {
                    DetalleOrder orden = new DetalleOrder();
                    orden.setNumero(result.getLong(1));
                    orden.setType(result.getString(2));
                    orden.setSize(result.getString(3));
                    orden.setCases(result.getString(4));
                    orden.setPacking(result.getString(5));
                    orden.setUnidad(result.getString(6));
                    orden.setDescripcion(result.getString(7));
                    orden.setMarca(result.getString(8));
                    orden.setCode_talla(result.getLong(9));
                    orden.setCode_Tipo(result.getLong(10));
                    orden.setCode_marca(result.getLong(11));
                    orden.setCode_packing(result.getLong(12));
                    orden.setStock(result.getLong(13));
                    orden.setEmpresa(result.getLong(14));
                    orden.setLinea(result.getLong(15));
                    orden.setOriginal(result.getString(16).charAt(0));
                    Listado_detalle.add(orden);
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Listado_detalle;
    }

    public DetalleOrder ConsultarDetalleOrden (long cod_empresa, long numero)
    {
        java.sql.ResultSet result = null;
        DetalleOrder orden = new DetalleOrder();
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("exec [android].[PA_Consultar_DetalleOrder] " + cod_empresa + "," + numero +";");

                while (result.next())
                {
                    orden.setNumero(result.getLong(1));
                    orden.setType(result.getString(2));
                    orden.setSize(result.getString(3));
                    orden.setCases(result.getString(4));
                    orden.setPacking(result.getString(5));
                    orden.setUnidad(result.getString(6));
                    orden.setDescripcion(result.getString(7));
                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return orden;
    }


    private String getConnectionUrl()
    {
        return url + serverName + ":" + portNumber + ";databaseName="
                + databaseName + ";selectMethod=" + selectMethod + ";";
    }

    public java.sql.Connection getConnection()
    {
        try {
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            connection = java.sql.DriverManager.getConnection(getConnectionUrl(),
                    userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /*
     * Display the driver properties, database details
     */

    public String EjecutarConsulta(String Consulta)
    {
        String nombre = null;
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery(Consulta);
                //closeConnection();
                while (result.next())
                {
                    nombre = result.getString(1);
                }
                result.close();
                result = null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            nombre = e.getMessage();
        }
        return nombre;
    }


    public void EjecutarQuery(String Consulta)
    {
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                select.executeQuery(Consulta);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public String EjecutarProcedimiento(byte[] foto)
    {
        String nombre = null;
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                //LLamando al procedimiento almacenado
                CallableStatement cstmt = connection.prepareCall("{call C113nt3.insertar_foto_lote(?)}");
                cstmt.setBytes("foto",foto);

                result = cstmt.executeQuery();
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            nombre = e.getMessage();
        }
        return nombre;
    }

    //public List<Plantas> regitrarInventario (Long emp,Long num, Long line,Long stock,Long tipo,Long talla,Long marca,Long cajas,Long Packing )
    public Boolean regitrarInventario (String sql )
    {
        Boolean resulado=false;
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery(sql);

                while (result.next())
                {
                    Long x =result.getLong(1);
                    if (x==1) {
                        resulado=true;
                    }

                }
                result.close();
                result = null;
                //closeConnection();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return resulado;
    }


    List<Cls_Photo> listaFotosEtiquetas = new ArrayList<Cls_Photo>();
    public List<Cls_Photo> ConsultarFotosEtiqueta (int numeroOrden)
    {
        List<Cls_Photo> listadoFotos = new ArrayList<Cls_Photo>();
        java.sql.ResultSet result = null;
        try {
            connection = this.getConnection();
            if (connection != null)
            {
                Statement select = connection.createStatement();
                result = select.executeQuery("exec [consultar].[PA_Purchase_OrderPhotosTags] " + String.valueOf(numeroOrden));

                while (result.next())
                {
                    Cls_Photo re_photo = new Cls_Photo();
                    re_photo.setNombre(result.getString(2));
                    re_photo.setUrl(result.getString(1));
                    listadoFotos.add(re_photo);
                }
                result.close();
                result = null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return listadoFotos;
    }


    public void closeConnection()
    {
        try {
            if (connection != null)
                connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



