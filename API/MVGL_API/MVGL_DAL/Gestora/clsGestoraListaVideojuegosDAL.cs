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
    public class clsGestoraListaVideojuegosDAL
    {
        private clsMyConnection conexionBase;

        public clsGestoraListaVideojuegosDAL()
        {
            conexionBase = new clsMyConnection();
        }
        /// <summary>
        ///<b>Cabecera:</b> public int borrarVideojuegoDeListaDAL(String idUsuario, int idVideojuego) <br />
        ///<b>Descripción:</b> Este metodo se encarga de borrar el videojuego que corresponda con el parametro "idVideojuego" de la lista del usuario que corresponda con el parametro "idUsuario" <br />
        ///<b> Precondiciones:</b> El videojuego se debe estar en la lista del usuario <br />
        ///<b> Postcondiciones:</b> Se borrara el videojuego de la lista del usuario introducido por parametro <br />
        ///<b>Entrada:</b> String idUsuario. El idUsuario del cual se desea borrar el videojuego <br />
        ///int idVideojuego. El id del videojuego el cual se desea borrar de la lista del usuario <br />
        ///<b>Salida:</b> int resultado. El número de filas afectadas, el resultado esperado debería ser 4 (porque se modifican 4 filas debido a los triggers)<br />
        /// </summary>
        /// <param name="idUsuario"><b>idUsuario - String. </b>El id del usuario del cual se desea borrar el videojuego</param>
        /// <param name="idVideojuego"><b>idVideojuego - id. </b>El id del videojuego el cual se desea borrar de la lista del usuario</param>
        /// <returns><b>resultado - int.</b>El número de filas borradas en la BBDD como resultado de la instrucción, el resultado correcto es 4 (porque se modifican 4 filas debido a los triggers), cualquier otro resultado puede significar un error</returns>
        public int borrarVideojuegoDeListaDAL(String idUsuario, int idVideojuego)
        {
            int resultado = 0;
            clsMyConnection conexionBase = new clsMyConnection();
            SqlCommand miComando = new SqlCommand();
            SqlConnection conexionEstablecida;
            miComando.Parameters.Add("@idVideojuego", System.Data.SqlDbType.Int).Value = idVideojuego;
            miComando.Parameters.Add("@idUsuario", System.Data.SqlDbType.VarChar).Value = idUsuario;
            miComando.CommandText = "DELETE FROM ListaVideojuegos WHERE IdUsuario = @idUsuario AND IdVideojuego = @idVideojuego";

            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;

            resultado = miComando.ExecuteNonQuery();

            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
        /// <summary>
        ///<b>Cabecera:</b> public int editarVideojuegoDeListaDAL(clsListaVideojuego oListaVideojuego) <br />
        ///<b>Descripción:</b> Este metodo se encarga de editar el registro de la BBDD que corresponda con el elemento de la lista de videojuegos pasado por parametros <br />
        ///<b> Precondiciones:</b> El videojuego ya se debe encontrar en la lista <br />
        ///<b> Postcondiciones:</b> El elemento con el que coincidan los ids introducidos por parametros mediante el objeto sera modificado con los nuevos valores <br />
        ///<b>Entrada:</b> clsListaVideojuego oListaVideojuego. El videojuego a editar de la lista de videojuegos <br />
        ///<b>Salida:</b> int resultado. El número de filas afectadas, el resultado esperado debería ser 4 (porque se modifican 4 filas debido a los triggers) <br />
        /// </summary>
        /// <param name="oListaVideojuego"><b>oListaVideojuego - clsListaVideojuego. </b> El videojuego a editar de la lista de videojuegos, se editara el registro que coincida con los ids del objeto pasado por parametros</param>
        /// <returns><b>resultado - int.</b>El número de filas borradas en la BBDD como resultado de la instrucción, el resultado correcto es 4 (porque se modifican 4 filas debido a los triggers), cualquier otro resultado puede significar un error</returns>
        public int editarVideojuegoDeListaDAL(clsListaVideojuego oListaVideojuego)
        {
            int resultado = 0;
            clsMyConnection conexionBase = new clsMyConnection();
            SqlCommand miComando = new SqlCommand();
            SqlConnection conexionEstablecida;
            miComando.Parameters.Add("@idUsuario", System.Data.SqlDbType.VarChar).Value = oListaVideojuego.IdUsuario;
            miComando.Parameters.Add("@idVideojuego", System.Data.SqlDbType.Int).Value = oListaVideojuego.IdVideojuego;
            miComando.Parameters.Add("@estado", System.Data.SqlDbType.Int).Value = oListaVideojuego.Estado;
            if (oListaVideojuego.FechaDeComienzo == null)
            { 
                miComando.Parameters.Add("@fechaDeComienzo", System.Data.SqlDbType.SmallDateTime).Value = System.DBNull.Value;
            }
            else 
            {
                miComando.Parameters.Add("@fechaDeComienzo", System.Data.SqlDbType.SmallDateTime).Value = oListaVideojuego.FechaDeComienzo;
            }
            if (oListaVideojuego.FechaDeFinalizacion == null)
            {
                miComando.Parameters.Add("@fechaDeFinalizacion", System.Data.SqlDbType.SmallDateTime).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@fechaDeFinalizacion", System.Data.SqlDbType.SmallDateTime).Value = oListaVideojuego.FechaDeFinalizacion;
            }
            if (oListaVideojuego.Nota == 0) //si la nota es 0 significa que el usuario no le ha puesto nota al juego, ya que la nota minima es 1, con lo cual, le pasamos el valor a nulo para que al hacer la media de la nota, la nota a nulo no cuente
            {
                miComando.Parameters.Add("@nota", System.Data.SqlDbType.Int).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@nota", System.Data.SqlDbType.Int).Value = oListaVideojuego.Nota;
            }
            if (oListaVideojuego.Dificultad == 0) //esto sigue el mismo funcionamiento que con la nota, la ponemos a NULL ya que el valor minimo es 1
            {
                miComando.Parameters.Add("@dificultad", System.Data.SqlDbType.Int).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@dificultad", System.Data.SqlDbType.Int).Value = oListaVideojuego.Dificultad;
            }
            miComando.CommandText = "UPDATE ListaVideojuegos SET FechaDeComienzo = @fechaDeComienzo, FechaDeFinalizacion = @fechaDeFinalizacion, " +
                "Nota = @nota, Dificultad = @dificultad, Estado = @estado WHERE IdUsuario = @idUsuario AND IdVideojuego = @idVideojuego";

            conexionEstablecida = conexionBase.getConnection(); //pongo esto aqui para tener la conexion abierta el menor tiempo posible
            miComando.Connection = conexionEstablecida;

            resultado = miComando.ExecuteNonQuery();

            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
        /// <summary>
        ///<b>Cabecera:</b> public int insertarVideojuegoEnListaDAL(clsListaVideojuego oVideojuegoEnLista) <br />
        ///<b>Descripción:</b> Este metodo se encarga de insertar un videojuego en la lista de un usuario de la BBDD <br />
        ///<b> Precondiciones:</b> El usuario y el videojuego deben existir, el juego no puede existir previamente en la lista del usuario <br />
        ///<b> Postcondiciones:</b> Se insertara el videojuego en la lista del usuario <br />
        ///<b>Entrada:</b> clsListaVideojuego oVideojuegoEnLista. El videojuego y la lista en la que se desea insertar el videojuego <br />
        ///<b>Salida:</b> int resultado. El numero de filas afectadas por la insercion <br />
        /// </summary>
        /// <param name="oVideojuegoEnLista"><b>oVideojuegoEnLista - clsListaVideojuego. </b> El videojuego y la lista en la que se desea insertar</param>
        /// <returns><b>resultado - int.</b>El numero de filas afectadas por la instruccion de insercion. El resultado esperado es 4 (porque se modifican 4 filas debido a los triggers), cualquier otro resultado podria significar error</returns>
        public int insertarVideojuegoEnListaDAL(clsListaVideojuego oVideojuegoEnLista)
        {
            int resultado = 0;
            clsMyConnection conexionBase = new clsMyConnection();
            SqlCommand miComando = new SqlCommand();
            SqlConnection conexionEstablecida;
            miComando.Parameters.Add("@idUsuario", System.Data.SqlDbType.VarChar).Value = oVideojuegoEnLista.IdUsuario;
            miComando.Parameters.Add("@idVideojuego", System.Data.SqlDbType.Int).Value = oVideojuegoEnLista.IdVideojuego;
            miComando.Parameters.Add("@estado", System.Data.SqlDbType.Int).Value = oVideojuegoEnLista.Estado;
            if (oVideojuegoEnLista.FechaDeComienzo == null)
            {
                miComando.Parameters.Add("@fechaDeComienzo", System.Data.SqlDbType.SmallDateTime).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@fechaDeComienzo", System.Data.SqlDbType.SmallDateTime).Value = oVideojuegoEnLista.FechaDeComienzo;
            }
            if (oVideojuegoEnLista.FechaDeFinalizacion == null)
            {
                miComando.Parameters.Add("@fechaDeFinalizacion", System.Data.SqlDbType.SmallDateTime).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@fechaDeFinalizacion", System.Data.SqlDbType.SmallDateTime).Value = oVideojuegoEnLista.FechaDeFinalizacion;
            }
            if (oVideojuegoEnLista.Nota == 0) //si la nota es 0 significa que el usuario no le ha puesto nota al juego, ya que la nota minima es 1, con lo cual, le pasamos el valor a nulo para que al hacer la media de la nota, la nota a nulo no cuente
            {
                miComando.Parameters.Add("@nota", System.Data.SqlDbType.Int).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@nota", System.Data.SqlDbType.Int).Value = oVideojuegoEnLista.Nota;
            }
            if (oVideojuegoEnLista.Dificultad == 0) //esto sigue el mismo funcionamiento que con la nota, la ponemos a NULL ya que el valor minimo es 1
            {
                miComando.Parameters.Add("@dificultad", System.Data.SqlDbType.Int).Value = System.DBNull.Value;
            }
            else
            {
                miComando.Parameters.Add("@dificultad", System.Data.SqlDbType.Int).Value = oVideojuegoEnLista.Dificultad;
            }
            miComando.CommandText = "INSERT INTO ListaVideojuegos(IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad, Estado) VALUES" +
                "(@idUsuario, @idVideojuego, @fechaDeComienzo, @fechaDeFinalizacion, @nota, @dificultad, @estado)";

            conexionEstablecida = conexionBase.getConnection(); //pongo esto aqui para tener la conexion abierta el menor tiempo posible
            miComando.Connection = conexionEstablecida;

            resultado = miComando.ExecuteNonQuery();

            conexionBase.closeConnection(ref conexionEstablecida);

            return resultado;
        }
    }
}
