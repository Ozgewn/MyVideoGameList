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
    public class clsListadoEstadosDAL
    {
        private clsMyConnection conexionBase;
        private SqlConnection conexionEstablecida;

        public clsListadoEstadosDAL()
        {
            conexionBase = new clsMyConnection();
        }
        /// <summary>
        /// <b>Cabecera:</b> public List<clsEstado> getListadoCompletoEstadosDAL() <br />
        /// <b>Descripción:</b> Este metodo se encarga de traer una lista completa de todos los posibles estados
        /// de un videojuego dentro de una lista de usuario<br />
        /// <b>Precondiciones:</b> N/A <br />
        /// <b>Postcondiciones:</b> Se devolvera una lista con el id y el nombre de cada estado <br />
        /// <b>Entrada:</b> N/A <br />
        /// <b>Salida:</b> List<clsEstado> listadoCompletoEstados. Un listado completo de todos los posibles estados
        /// de un videojuego dentro de una lista de usuario, traido desde la BBDD<br />
        /// </summary>
        /// <returns><b>listadoCompletoEstados - List<clsEstado>. </b> Listado completo de los posibles estados de un videojuego</returns>
        public List<clsEstado> getListadoCompletoEstadosDAL()
        {
            List<clsEstado> listadoCompletoEstados = new List<clsEstado>();
            clsEstado oEstado;
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.CommandText = "SELECT * FROM EstadosVideojuego";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows)
                {
                    while (miLector.Read())
                    {
                        oEstado = new clsEstado();
                        oEstado.Id = (int)miLector["Id"];
                        oEstado.NombreEstado = (String)miLector["NombreEstado"];
                        listadoCompletoEstados.Add(oEstado);
                    }
                }
                miLector.Close();
                conexionBase.closeConnection(ref conexionEstablecida);
            }
            catch (SqlException)
            {
                throw;
            }

            return listadoCompletoEstados;
        }
    }
}
