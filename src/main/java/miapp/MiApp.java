package miapp;

import static java.util.Collections.emptyList;
import java.net.URL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import java.util.List;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


/*
documentancion official de XMLRPC

https://www.odoo.com/documentation/17.0/developer/reference/external_api.html#:~:text=Odoo%20requires%20users?msockid=0109e3f5f6776f75312ef775f7e36ec1

 */
public class MiApp {

    public static void main(String[] args) {

        try {

            final XmlRpcClient client = new XmlRpcClient();

            String url = "https://domain.com",
                    db = "db_name",
                    username = "user@domain.com.do",
                    password = "000000";

// String url =  "http://10.0.0.28:8069",
//       	db= "odoo",
//       	username="admin",
//          password="2024";

            final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
            common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", url)));
            client.execute(common_config, "version", emptyList());

            int uid = (int) client.execute(common_config, "authenticate", asList(db, username, password, emptyMap()));

            //  System.err.println(uid);  //aca imprimo mi sesion id 
            // conection con object models
            final XmlRpcClient models = new XmlRpcClient() {
                {
                    setConfig(new XmlRpcClientConfigImpl() {
                        {
                            setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                        }
                    });
                }
            };
            //  fin de conection con object models

            //***************** |  Query 1 |*************************************
            // --------------| lista de ps_id  |------------------------
            final List puertos_id = asList((Object[]) models.execute(
                    "execute_kw", asList(
                            db, uid, password,
                            "custom.port", "search",
                            asList(asList( //asList("is_company", "=", true),
                                    //asList("customer", "=", true)
                                    )))));

            //System.err.println( puertos_id);   //aca muestra lista de puertos id
            //-------------------fin de  ps_id----------------------
            //---------------------| puertos |-------------------------
            final List puertos = asList((Object[]) models.execute(
                    "execute_kw", asList(
                            db, uid, password,
                            "custom.port", "read",
                            asList(puertos_id)
                    )
            ));

            System.err.println(puertos.get(0));   // aca llamo a el inidice 0 del listado de puertos

            //***************** Fin de  Query 1*************************************
            
            //***************** |  Query 2  |*************************************
            // --------------| lista de serial_id  |------------------------
            final List serials_id = asList((Object[]) models.execute(
                    "execute_kw", asList(
                            db, uid, password,
                            "serial.tracking", "search",
                            asList(asList( //asList("is_company", "=", true),
                                    //asList("customer", "=", true)
                                    )))));

            //System.err.println( serials_id);       //aca imprimo todos los id de los serial tracking
            //-------------------fin de  serial_id----------------------
            
            // --------------| contar lista de serial_id  |------------------------
            final List serialRecords = asList((Object[]) models.execute(
                    "execute_kw", asList(
                            db, uid, password,
                            "serial.tracking", "read",
                            //asList(serials_id)
                            asList(8880)
                    )
            ));

            //System.err.println(8880);   //hay muchas por eso lo filtro por este id que consulte arriba en la lista
            //  System.err.println( serialRecords.get(0));   // aca llamo a el inidice 0 del listado de serials
            //-------------------fin de  serial_id----------------------
            //*****************  Fin Query 2  *************************************
            //*********************Contar cantidad (length)***************************
            
            // --------------| contar lista de serial_id  |------------------------
            final List countsserial_id = asList((Object[]) models.execute(
                    "execute_kw", asList(
                            db, uid, password,
                            "serial.tracking", "search_count",
                            asList(asList( //asList("is_company", "=", true),
                                    //asList("customer", "=", true)
                                    )))));

            // System.err.println( countsserial_id);                 // aca  imprimo la cantidad de serial id que hay 
            //-------------------fin de  serial_id----------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
