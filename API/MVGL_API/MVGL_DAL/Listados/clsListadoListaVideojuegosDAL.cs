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
    public class clsListadoListaVideojuegosDAL
    {
        private clsMyConnection conexionBase;
        private SqlConnection conexionEstablecida;

        public clsListadoListaVideojuegosDAL()
        {
            conexionBase = new clsMyConnection();
        }

        /// <summary>
        /// <b>Cabecera:</b> public List<clsListaVideojuego> getListaVideojuegosDeUsuarioDAL(String idUsuario)<br />
        /// <b>Descripción:</b> Este metodo se encarga de devolver la lista de videojuegos del usuario introducido por parametros<br />
        /// <b>Precondiciones:</b> El usuario debe tener al menos 1 juego en su lista, el idusuario debe ser válido<br />
        /// <b>Postcondiciones:</b> Se devolverá la lista de videojuegos del usuario introducido por parametros<br />
        /// <b>Entrada:</b> String idUsuario. El id del usuario del cual se desea obtener su lista de videojuegos<br />
        /// <b>Salida:</b>  List<clsListaVideojuegos> listadoVideojuegosDeUsuario. El listado de videojuegos del usuario<br />
        /// </summary>
        /// <param name="idUsuario"><b>idUsuario - int. <b/>El id del usuario del cual se desea obtener la lista de videojuegos</param>
        /// <returns><b>listadoVideojuegosDeUsuario - List<clsListaVideojuego>.</b> La lista de videojuegos completa traida de la BBDD que corresponde al id del usuario introducido por parametros</returns>
        public List<clsListaVideojuego> getListaVideojuegosDeUsuarioDAL(String idUsuario)
        {
            List<clsListaVideojuego> listadoVideojuegosDeUsuario = new List<clsListaVideojuego>();
            clsListaVideojuego oListaVideojuego;
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.Parameters.Add("@idUsuario", System.Data.SqlDbType.VarChar).Value = idUsuario;
            miComando.CommandText = "SELECT * FROM ListaVideojuegos WHERE IdUsuario = @idUsuario";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows)
                {
                    while (miLector.Read())
                    {
                        oListaVideojuego = new clsListaVideojuego();
                        oListaVideojuego.IdUsuario = (String)miLector["IdUsuario"];
                        oListaVideojuego.IdVideojuego = (int)miLector["IdVideojuego"];
                        if (miLector["FechaDeComienzo"] != System.DBNull.Value)
                        {
                            oListaVideojuego.FechaDeComienzo = (DateTime)miLector["FechaDeComienzo"];
                        }
                        else
                        {
                            oListaVideojuego.FechaDeComienzo = null; //este else lo hago porque necesito inicializarlo a null, si no, DateTime se inicializa automaticamente con el valor de DateTime.MinValue
                        }
                        if (miLector["FechaDeFinalizacion"] != System.DBNull.Value)
                        {
                            oListaVideojuego.FechaDeFinalizacion = (DateTime)miLector["FechaDeFinalizacion"];
                        }
                        else
                        {
                            oListaVideojuego.FechaDeFinalizacion = null;
                        }
                        if (miLector["Nota"] != System.DBNull.Value)
                        {
                            oListaVideojuego.Nota = (int)miLector["Nota"];
                        }
                        if (miLector["Dificultad"] != System.DBNull.Value)
                        {
                            oListaVideojuego.Dificultad = (int)miLector["Dificultad"];
                        }
                        oListaVideojuego.Estado = (int)miLector["Estado"];
                        listadoVideojuegosDeUsuario.Add(oListaVideojuego);
                    }
                }
                miLector.Close();
                conexionBase.closeConnection(ref conexionEstablecida);
            }
            catch (SqlException)
            {
                throw;
            }

            return listadoVideojuegosDeUsuario;
        }
    }
}