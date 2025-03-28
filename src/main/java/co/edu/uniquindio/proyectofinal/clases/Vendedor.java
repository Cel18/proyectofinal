package co.edu.uniquindio.proyectofinal.clases;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import co.edu.uniquindio.proyectofinal.excepciones.AutoCompraException;
import co.edu.uniquindio.proyectofinal.excepciones.ProductoCanceladoOVendidoException;

public class Vendedor implements Serializable {
   private String nombre;
   private String apellido; 
   private String cedula;
   private String direccion;
   private Muro muro;
   private static List<Vendedor> contactos = new ArrayList<>();
   private static List<Producto> productos = new ArrayList<>();
   private static List<Solicitud> solicitudesPendientes = new ArrayList<>();
   private static List<Solicitud> solicitudesRechazadas = new ArrayList<>();
   private static List <Producto> productosVendidos = new ArrayList<>();
   private static List <Solicitud> solicitudesAceptadas = new ArrayList<>();

    //Constuctor

    public Vendedor(String nombre, String apellido, String cedula, String direccion, Muro muro) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccion = direccion;
        this.muro = muro;
    }

    //Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Muro getMuro() {
        return muro;
    }

    public void setMuro(Muro muro) {
        this.muro = muro;
    }

    public List<Vendedor> getContactos() {
        return contactos;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    //Métodos

    @SuppressWarnings("static-access")
    public void agregarContacto(Vendedor vendedor, Solicitud solicitud) throws IOException {
        if (!contactos.contains(vendedor) | solicitudesPendientes.contains(solicitud)) {
            Utilidades.getInstance().escribirLog(Level.INFO, "Función agregarContacto en Vendedor: Funcionamiento adecuado");
            contactos.add(solicitud.getEmisor());
            solicitud.getEmisor().getContactos().add(this);
            solicitudesAceptadas.add(solicitud);
            solicitudesPendientes.remove(solicitud);
            solicitud.getEmisor().guardarContactos();
            this.guardarContactos();
            this.guardarSolicitudesPendientes();
            this.guardarSolicitudesAceptadas();
            this.GuardarSolicitudesPendientesXML();
            this.GuardarSolicitudesAceptadasXML();
            
        }
    }

    public void agregarProducto(Producto producto){
        Utilidades.getInstance().escribirLog(Level.INFO, "Función agregarProducto en Vendedor: Funcionamiento adecuado");
        productos.add(producto);
    }

    //Persistencia
    
    public static void guardarProductos() {
        try (ObjectOutputStream oosProductos = new ObjectOutputStream(new FileOutputStream("productos.dat"))) {
            oosProductos.writeObject(productos);
            System.out.println("Productos guardados en productos.dat exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void guardarProductosVendidos() throws IOException {
        try (ObjectOutputStream oosProductosVendidos = new ObjectOutputStream(new FileOutputStream("productosVendidos.dat"));
            ){
            oosProductosVendidos.writeObject(productosVendidos);
        }
    }

    public static void guardarContactos() throws IOException {
        try (ObjectOutputStream oosContactos = new ObjectOutputStream(new FileOutputStream("contactos.dat"));
            ){
            oosContactos.writeObject(contactos);
        }
    }

    public static void guardarSolicitudesPendientes() throws IOException {
        try (ObjectOutputStream oosSolicitudesPendientes = new ObjectOutputStream(new FileOutputStream("solicitudesPendientes.dat"));
            ){
            oosSolicitudesPendientes.writeObject(solicitudesPendientes);
        }
    }

    public static void guardarSolicitudesRechazadas() throws IOException {
        try (ObjectOutputStream oosSolicitudesRechazadas = new ObjectOutputStream(new FileOutputStream("solicitudesRechazadas.dat"));
            ){
            oosSolicitudesRechazadas.writeObject(solicitudesRechazadas);

        }
    }

    public static void guardarSolicitudesAceptadas() throws IOException {
        try (ObjectOutputStream oosSolicitudesAceptadas = new ObjectOutputStream(new FileOutputStream("solicitudesAceptadas.dat"));
            ){
            oosSolicitudesAceptadas.writeObject(solicitudesAceptadas);

        }
    }

    public static void GuardarSolicitudesAceptadasXML () throws IOException{
        String Filepath = "C:\\Users\\Epubl\\Downloads\\Proyecto Final Programación III\\proyectofinal\\solicitudesAceptadas.xml";
    
        try(BufferedWriter xmlWriter = new BufferedWriter (new FileWriter(Filepath))){
    
            xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlWriter.write("<solicitudes>\n");
    
            for(Solicitud solicitud: solicitudesAceptadas){
                Vendedor emisor = solicitud.getEmisor();
                Vendedor receptor = solicitud.getReceptor();
                
                xmlWriter.write("\t<solicitud>\n");
                xmlWriter.write("\t\t<Emisor>\n");
                xmlWriter.write("\t\t\t<Nombre>" + emisor.getNombre() + "</Nombre>\n");
                xmlWriter.write("\t\t\t<Apellido>" + emisor.getApellido() + "</Apellido>\n");
                xmlWriter.write("\t\t\t<Cedula>" + emisor.getCedula() + "</Cedula>\n");
                xmlWriter.write("\t\t\t<Direccion>" + emisor.getDireccion() + "</Direccion>\n");
                xmlWriter.write("\t\t</Emisor>\n");
                
                xmlWriter.write("\t\t<Receptor>\n");
                xmlWriter.write("\t\t\t<Nombre>" + receptor.getNombre() + "</Nombre>\n");
                xmlWriter.write("\t\t\t<Apellido>" + receptor.getApellido() + "</Apellido>\n");
                xmlWriter.write("\t\t\t<Cedula>" + receptor.getCedula() + "</Cedula>\n");
                xmlWriter.write("\t\t\t<Direccion>" + receptor.getDireccion() + "</Direccion>\n");
                xmlWriter.write("\t\t</Receptor>\n");
                xmlWriter.write("\t</solicitud>\n");
            }
            
            xmlWriter.write("</solicitudes>\n");
        }

        }

        public static void GuardarSolicitudesRechazadasXML() throws IOException {
    String Filepath = "C:\\Users\\Epubl\\Downloads\\Proyecto Final Programación III\\proyectofinal\\solicitudesRechazadas.xml";

    try (BufferedWriter xmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Filepath), "UTF-8"))) {

        xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlWriter.write("<solicitudes>\n");

        for (Solicitud solicitud : solicitudesRechazadas) {
            xmlWriter.write("\t<solicitud>\n");

            xmlWriter.write("\t\t<Emisor>\n");
            xmlWriter.write("\t\t\t<Nombre>" + solicitud.getEmisor().getNombre() + "</Nombre>\n");
            xmlWriter.write("\t\t\t<Apellido>" + solicitud.getEmisor().getApellido() + "</Apellido>\n");
            xmlWriter.write("\t\t</Emisor>\n");

            xmlWriter.write("\t\t<Receptor>\n");
            xmlWriter.write("\t\t\t<Nombre>" + solicitud.getReceptor().getNombre() + "</Nombre>\n");
            xmlWriter.write("\t\t\t<Apellido>" + solicitud.getReceptor().getApellido() + "</Apellido>\n");
            xmlWriter.write("\t\t\t<Cedula>" + solicitud.getReceptor().getCedula() + "</Cedula>\n");
            xmlWriter.write("\t\t</Receptor>\n");

            xmlWriter.write("\t</solicitud>\n");
        }

        xmlWriter.write("</solicitudes>");
    } catch (IOException e) {
        e.printStackTrace();
        throw new IOException("Error al escribir el archivo XML de solicitudes rechazadas.", e);
    }
}



    
        public static void GuardarProductosXML () throws IOException{
            String Filepath = "C:\\Users\\Epubl\\Downloads\\Proyecto Final Programación III\\proyectofinal\\Productos.xml";
    
            try(BufferedWriter xmlWriter = new BufferedWriter (new FileWriter(Filepath))){
    
                xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                xmlWriter.write("<Productos>\n");
    
                for(Producto producto: productos){
                    xmlWriter.write("\t<producto>\n");
                    xmlWriter.write("\t\t <Nombre>" + producto.getNombre() + "</Nombre>\n");
                    xmlWriter.write("\t\t <Codigo>" + producto.getCodigo() + "</Codigo> \n");
                    xmlWriter.write("\t</producto>\n");
    
    
    
                }
                xmlWriter.write("</Productos>\n");
                }
    
            }


            public static void GuardarProductosVendidosXML () throws IOException{
                String Filepath = "C:\\Users\\Epubl\\Downloads\\Proyecto Final Programación III\\proyectofinal\\ProductosVendidos.xml";
        
                try(BufferedWriter xmlWriter = new BufferedWriter (new FileWriter(Filepath))){
        
                    xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    xmlWriter.write("<Productos>\n");
        
                    for(Producto producto: productosVendidos){
                        xmlWriter.write("\t<producto>\n");
                        xmlWriter.write("\t\t <Nombre>" + producto.getNombre() + "</Nombre>\n");
                        xmlWriter.write("\t\t <Codigo>" + producto.getCodigo() + "</Codigo> \n");
                        xmlWriter.write("\t</producto>\n");
        
        
        
                    }
                    xmlWriter.write("</Productos>\n");
                    }
        
                }


        


    public static void GuardarSolicitudesPendientesXML () throws IOException{
        String Filepath = "C:\\Users\\Epubl\\Downloads\\Proyecto Final Programación III\\proyectofinal\\solicitudesPendientes.xml";

        try(BufferedWriter xmlWriter = new BufferedWriter (new FileWriter(Filepath))){

            xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlWriter.write("<solicitudes>\n");

            for(Solicitud solicitud: solicitudesPendientes){
                xmlWriter.write("\t<solicitud>\n");
                xmlWriter.write("\t\t <Emisor>" + solicitud.getEmisor().getNombre() + "</Emisor>\n");
                xmlWriter.write("\t\t <Receptor>" + solicitud.getReceptor().getNombre() + "</Receptor> \n");
                xmlWriter.write("\t</solicitud>\n");



            }
            xmlWriter.write("</solicitudes>\n");
            }

        }
    
    
    
    public void enviarSolicitud (Vendedor destino) throws IOException{
        Solicitud solicitud = new Solicitud(this, destino);
        destino.recibirSolicitud(solicitud);
    }

    public void recibirSolicitud (Solicitud solicitud) throws IOException{
        solicitudesPendientes.add(solicitud);
        Vendedor.guardarSolicitudesPendientes();
    }



//SolicitudNoEncontradaException pendiente
    @SuppressWarnings("static-access")
    public void rechazarSolicitud (Solicitud solicitud) throws IOException{
        if (solicitudesPendientes.contains(solicitud)){
            Utilidades.getInstance().escribirLog(Level.INFO, "Función rechazarSolicitud en Vendedor: Funcionamiento adecuado");
            solicitudesPendientes.remove(solicitud);
            solicitudesRechazadas.add(solicitud);
            this.guardarSolicitudesPendientes();
            this.guardarSolicitudesRechazadas();
            this.GuardarSolicitudesPendientesXML();
            this.GuardarSolicitudesRechazadasXML();
            solicitud.getEmisor().guardarSolicitudesRechazadas();

            
            
            

}
}
    public void agregarProductoVendido (Producto producto){
        productosVendidos.add(producto);
    }

    @SuppressWarnings("static-access")
    public void comprarProducto (Producto producto) throws ProductoCanceladoOVendidoException, AutoCompraException, IOException {
        Vendedor vendedorOriginal = producto.getAutor();

        if(!vendedorOriginal.equals(this)){
            if (producto.getEstado() == EstadoProducto.PUBLICADO){
                producto.setEstadoProducto(EstadoProducto.VENDIDO);
                vendedorOriginal.eliminarProducto(producto);
                vendedorOriginal.agregarProductoVendido(producto);
                vendedorOriginal.guardarProductos();
                vendedorOriginal.guardarProductosVendidos();
                vendedorOriginal.GuardarProductosVendidosXML();
                vendedorOriginal.GuardarProductosXML();
            }
            else{
                throw new ProductoCanceladoOVendidoException("El producto ya ha sido vendido o ya no está disponible");
            } 
        } else{ 
            throw new AutoCompraException("No se permite autocomprar productos");

        }
    }

    public void eliminarProducto (Producto producto){
        productos.remove(producto);
    }


}

