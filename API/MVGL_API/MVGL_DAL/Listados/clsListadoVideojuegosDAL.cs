using MVGL_DAL.Conexion;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_DAL.Listados
{
    public class clsListadoVideojuegosDAL
    {
        private clsMyConnection conexionBase;
        private SqlConnection conexionEstablecida;

        public clsListadoVideojuegosDAL()
        {
            conexionBase = new clsMyConnection();
        }
        /// <summary>
        ///<b>Cabecera:</b> public clsVideojuego getVideojuegoSegunIdDAL(String id)<br />
        ///<b>Descripción:</b> Este metodo se encarga de devolver la información correspondiente al videojuego introducido por parametros<br />
        ///<b> Precondiciones:</b> El id del videojuego debe corresponder a un videojuego existente<br />
        ///<b> Postcondiciones:</b> Se devolvera la informacion del videojuego en cuestion<br />
        ///<b>Entrada:</b> String id. El id del videojuego del cual se desea obtener la informacion<br />
        ///<b>Salida:</b> clsVideojuego oVideojuego. El videojuego con toda su respectiva informacion<br />
        /// </summary>
        /// <param name="id"><b>id - String. </b>El id del videojuego del cual se desea obtener la informacion de la BBDD</param>
        /// <returns><b>oVideojuego - clsVideojuego. </b>El videojuego traido de la BBDD que corresponde con el id que se ha introducido por parametros</returns>
        public clsVideojuego getVideojuegoSegunIdDAL(String id)
        {
            clsVideojuego oVideojuego = new clsVideojuego();
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.Parameters.Add("@id", System.Data.SqlDbType.VarChar).Value = id;
            miComando.CommandText = "SELECT * FROM Videojuegos WHERE Id = @id";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows)
                {
                    miLector.Read(); //solo un read porque la instruccion sql solo nos devolvera una fila
                    oVideojuego.Id = (int)miLector["Id"]; //no controlo nulos, porque estos datos (los videojuegos) los inserta el administrador (yo), y además, de que no insertaré datos nulos donde no deba, la BBDD no lo permite
                    oVideojuego.Nombre = (String)miLector["Nombre"];
                    oVideojuego.Desarrollador = (String)miLector["Desarrollador"];
                    oVideojuego.Distribuidores = (String)miLector["Distribuidores"];
                    oVideojuego.Plataformas = (String)miLector["Plataformas"];
                    if (miLector["NotaMedia"] != System.DBNull.Value)
                    {
                        oVideojuego.NotaMedia = (double)miLector["NotaMedia"];
                    }
                    if (miLector["DificultadMedia"] != System.DBNull.Value)
                    {
                        oVideojuego.DificultadMedia = (double)miLector["DificultadMedia"];
                    }
                    oVideojuego.FechaDeLanzamiento = (DateTime)miLector["FechaDeLanzamiento"];
                    oVideojuego.Generos = (String)miLector["Generos"];
                    oVideojuego.UrlImagen = (String)miLector["urlImagen"];
                }
                miLector.Close();
                conexionBase.closeConnection(ref conexionEstablecida);
            }
            catch (SqlException)
            {
                throw;
            }
            return oUsuario;
        }
    }
}
