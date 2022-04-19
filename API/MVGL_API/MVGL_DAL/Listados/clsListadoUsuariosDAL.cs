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
    public class clsListadoUsuariosDAL
    {
        private clsMyConnection conexionBase;
        private SqlConnection conexionEstablecida;
        public clsListadoUsuariosDAL()
        {
            conexionBase = new clsMyConnection();
        }
        /// <summary>
        /// <b>Cabecera:</b> public clsUsuario getUsuarioSegunIdDAL(int id)<br />
        /// <b>Descripción:</b> Este método se encarga de obtener toda la información del usuario de la BBDD que corresponda con el id pasado por parámetros<br />
        /// <b>Precondiciones:</b> El usuario debe existir, el id del usuario debe ser válido<br />
        /// <b>Postcondiciones:</b> Se devolverá la información relevante del usuario que corresponda con el id introducido por parámetros<br />
        /// <b>Entrada:</b> String id. El id del usuario del cual se desea obtener la información<br />
        /// <b>Salida:</b> clsUsuario oUsuario. El objeto usuario con toda su respectiva información obtenida de la BBDD<br />
        /// </summary>
        /// <param name="id"><b>id - String</b>. El id del usuario del cuál se desea obtener la información </param>
        /// <returns><b>oUsuario - clsUsuario.</b> El usuario con toda la información</returns>
        public clsUsuario getUsuarioSegunIdDAL(String id)
        {
            clsUsuario oUsuario = new clsUsuario();
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.Parameters.Add("@id", System.Data.SqlDbType.VarChar).Value = id;
            miComando.CommandText = "SELECT * FROM Usuarios WHERE Id = @id";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows)
                {
                    miLector.Read(); //solo un read porque la instruccion sql solo nos devolvera una fila
                    oUsuario.Id = (String)miLector["Id"];
                    oUsuario.NombreUsuario = (String)miLector["NombreUsuario"];
                    oUsuario.VideojuegosJugados = (int)miLector["VideojuegosJugados"]; //no controlo nulos porque la BBDD no permite que aqui hayan nulos, y la BBDD lo controla poniendo 0 como valor por defecto
                    oUsuario.VideojuegosPlaneados = (int)miLector["VideojuegosPlaneados"];
                    oUsuario.VideojuegosDropeados = (int)miLector["VideojuegosDropeados"];
                    oUsuario.VideojuegosEnPausa = (int)miLector["VideojuegosEnPausa"];
                    oUsuario.VideojuegosJugando = (int)miLector["VideojuegosJugando"];
                    oUsuario.EsListaPrivada = (bool)miLector["esListaPrivada"];
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
