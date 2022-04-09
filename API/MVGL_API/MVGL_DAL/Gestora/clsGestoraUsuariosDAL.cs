using MVGL_DAL.Conexion;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_DAL.Gestora
{
    public class clsGestoraUsuariosDAL
    {
        private clsMyConnection conexionBase;
        private SqlConnection conexionEstablecida;

        public clsGestoraUsuariosDAL()
        {
            conexionBase = new clsMyConnection();
        }
        /// <summary>
        /// <b>Cabecera:</b>public int insertarUsuarioDAL(String id)<br />
        /// <b>Descripción:</b>Este método se encarga de insertar un usuario con el id correspondiente a la BBDD, con todos los datos con valores por defecto (0 todos)<br />
        /// <b>Precondiciones:</b>El id debe ser valido, y no exceder los 28 caracteres<br />
        /// <b>Postcondiciones:</b>El usuario se creera en la BBDD con su correspondiente id<br />
        /// <b>Entrada:</b>String id. El id del usuario a crear<br />
        /// <b>Salida:</b>int resultado. El número de filas afectadas, si es 1 significa que se ha insertado exitosamente, si es distinto de 1, habrá ocurrido algún fallo<br />
        /// </summary>
        /// <param name="id"><b>id - String. El id del usuario a crear en la BBDD</b> </param>
        /// <returns><b>resultado - int.El número de filas afectadas por la instrucción de inserción en la BBDD</b> </returns>
        public int insertarUsuarioDAL(String id)
        {
            int resultado = 0;
            SqlCommand miComando = new SqlCommand();
            miComando.Parameters.Add("@id", System.Data.SqlDbType.VarChar).Value = id;
            miComando.CommandText = "INSERT INTO Usuarios(Id) VALUES(@id)";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            resultado = miComando.ExecuteNonQuery();

            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
    }
}
