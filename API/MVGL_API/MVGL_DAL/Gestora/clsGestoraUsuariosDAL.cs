using MVGL_DAL.Conexion;
using MVGL_Entidades;
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
        /// <b>Cabecera:</b>public int insertarUsuarioBL(String id)<br />
        /// <b>Descripción:</b>Este método se encarga insertar un usuario, con todos los datos con valores por defecto (0 todos), menos el id y el nombre de usuario<br />
        /// <b>Precondiciones:</b>El id debe ser valido, y no exceder los 28 caracteres, el nombre de usuario no puede estar repetido, ni exceder los 30 caracteres<br />
        /// <b>Postcondiciones:</b>El usuario se creera en la BBDD con su correspondiente id<br />
        /// <b>Entrada:</b>clsUsuario oUsuario. El usuario a crear con su id (que debe coincidir con el id de Firebase) y el nombre de usuario (que no puede estar repetido)<br />
        /// <b>Salida:</b>int resultado. El número de filas afectadas, si es 1 significa que se ha insertado exitosamente, si es distinto de 1, habrá ocurrido algún fallo<br />
        /// </summary>
        /// <param name="oUsuario"></param>
        /// <returns><b>resultado - int.El número de filas afectadas por la instrucción de inserción en la BBDD</b> </returns>
        public int insertarUsuarioDAL(clsUsuario oUsuario)
        {
            int resultado = 0;
            SqlCommand miComando = new SqlCommand();
            miComando.Parameters.Add("@id", System.Data.SqlDbType.VarChar).Value = oUsuario.Id;
            miComando.Parameters.Add("@nombreUsuario", System.Data.SqlDbType.VarChar).Value = oUsuario.NombreUsuario;
            miComando.CommandText = "INSERT INTO Usuarios(Id, NombreUsuario) VALUES(@id, @nombreUsuario)";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            try
            {
                resultado = miComando.ExecuteNonQuery();

            }
            catch (Exception)
            {
                throw; //hago throw porque esto ya lo manejara la API
            }
            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
        /// <summary>
        /// <b>Cabecera:</b> public int editarUsuarioDAL(clsUsuario oUsuario) <br />
        /// <b>Descripción:</b> Este metodo se encarga de editar la informacion del usuario, mas concretamente, <br />
        /// si la lista es privada o no, solo modifica este valor ya que los demas campos se actualizaran automaticamente al añadir/borrar/editar un juego <br />
        /// de la lista del usuario <br />
        /// <b>Precondiciones:</b> El usuario debe existir <br />
        /// <b>Postcondiciones:</b> Los datos del usuario se modificaran <br />
        /// <b>Entrada:</b> clsUsuario oUsuario. El usuario a modificar con sus datos ya modificados para su proxima insercion <br />
        /// <b>Salida:</b> int resultado. El numero de filas afectadas por la edicion. El resultado esperado es 1, otro resultado podria significar error <br />
        /// </summary>
        /// <param name="oUsuario"><b>oUsuario - clsUsuario. </b> El usuario a modificar en la BBDD con sus datos ya modificados para su posterior edicion </param>
        /// <returns><b>resultado - int. </b> El número de filas afectadas por la instruccion de edicion, el resultado esperado es 1, cualquier otro resultado podria significar error </returns>
        public int editarUsuarioDAL(clsUsuario oUsuario)
        {
            int resultado = 0;
            clsMyConnection conexionBase = new clsMyConnection();
            SqlCommand miComando = new SqlCommand();
            //SqlConnection conexionEstablecida; si falla revisar esto
            miComando.Parameters.Add("@idUsuario", System.Data.SqlDbType.VarChar).Value = oUsuario.Id;
            miComando.Parameters.Add("@esListaPrivada", System.Data.SqlDbType.Bit).Value = oUsuario.EsListaPrivada;

            miComando.CommandText = "UPDATE Usuarios SET EsListaPrivada = @esListaPrivada WHERE Id = @idUsuario";

            conexionEstablecida = conexionBase.getConnection(); //pongo esto aqui para tener la conexion abierta el menor tiempo posible
            miComando.Connection = conexionEstablecida;

            try
            {
                resultado = miComando.ExecuteNonQuery();

            }
            catch (Exception)
            {
                throw; //hago throw porque esto ya lo manejara la API
            }

            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
        /// <summary>
        /// <b>Cabecera:</b> public int borrarUsuarioDAL(String id) <br />
        /// <b>Descripción:</b> Este metodo se encarga de borrar de la BBDD al usuario cuya id coincida con la introducida por parametros <br />
        /// <b>Precondiciones:</b> El usuario debe existir <br />
        /// <b>Postcondiciones:</b> El usuario sera borrado <br />
        /// <b>Entrada:</b> String id. El id del usuario a borrar <br />
        /// <b>Salida:</b> int resultado. El numero de filas borradas. Deberia ser 1, cualquier otro valor podria significar error <br />
        /// </summary>
        /// <param name="id"><b>id - String. </b>El id del usuario a borrar</param>
        /// <returns><b>resultado - int. </b>El numero de filas afectadas por la instruccion de borrado. El resultado esperado es 1, cualquier otro resultado podria significara error</returns>
        public int borrarUsuarioDAL(String id)
        {
            int resultado = 0;
            clsMyConnection conexionBase = new clsMyConnection();
            SqlCommand miComando = new SqlCommand();
            //SqlConnection conexionEstablecida; si falla revisar esto
            miComando.Parameters.Add("@idUsuario", System.Data.SqlDbType.Int).Value = id;

            miComando.CommandText = "DELETE FROM Usuarios WHERE Id = @idUsuario";

            conexionEstablecida = conexionBase.getConnection(); //pongo esto aqui para tener la conexion abierta el menor tiempo posible
            miComando.Connection = conexionEstablecida;

            try
            {
                resultado = miComando.ExecuteNonQuery();

            }
            catch (Exception)
            {
                throw; //hago throw porque esto ya lo manejara la API
            }

            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
    }
}
