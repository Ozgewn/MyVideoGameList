using MVGL_DAL.Gestora;
using MVGL_Entidades;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_BL.Gestora
{
    public class clsGestoraListaVideojuegosBL
    {
        /// <summary>
        ///<b>Cabecera:</b> public int borrarVideojuegoDeListaBL(String idUsuario, int idVideojuego) <br />
        ///<b>Descripción:</b> Este metodo se encarga de llamar a la DAL y que esta borre el videojuego que corresponda con el parametro "idVideojuego" de la lista del usuario que corresponda con el parametro "idUsuario" <br />
        ///<b> Precondiciones:</b> El videojuego se debe estar en la lista del usuario <br />
        ///<b> Postcondiciones:</b> Se borrara el videojuego de la lista del usuario introducido por parametro <br />
        ///<b>Entrada:</b> String idUsuario. El idUsuario del cual se desea borrar el videojuego <br />
        ///int idVideojuego. El id del videojuego el cual se desea borrar de la lista del usuario <br />
        ///<b>Salida:</b> int resultado. El número de filas afectadas, el resultado esperado debería ser 4 (porque se modifican 4 filas debido a los triggers) <br />
        /// </summary>
        /// <param name="idUsuario"><b>idUsuario - String. </b>El id del usuario del cual se desea borrar el videojuego</param>
        /// <param name="idVideojuego"><b>idVideojuego - id. </b>El id del videojuego el cual se desea borrar de la lista del usuario</param>
        /// <returns><b>resultado - int.</b>El número de filas borradas en la BBDD como resultado de la instrucción, el resultado correcto es 4 (porque se modifican 4 filas debido a los triggers), cualquier otro resultado puede significar un error</returns>
        public int borrarVideojuegoDeListaBL(String idUsuario, int idVideojuego)
        {
            return new clsGestoraListaVideojuegosDAL().borrarVideojuegoDeListaDAL(idUsuario, idVideojuego);
        }
        /// <summary>
        ///<b>Cabecera:</b> public int editarVideojuegoDeListaBL(clsListaVideojuego oListaVideojuego) <br />
        ///<b>Descripción:</b> Este metodo se encarga de llamar a la DAL y que esta edite el registro de la BBDD que corresponda con el elemento de la lista de videojuegos pasado por parametros <br />
        ///<b> Precondiciones:</b> El videojuego ya se debe encontrar en la lista <br />
        ///<b> Postcondiciones:</b> El elemento con el que coincidan los ids introducidos por parametros mediante el objeto sera modificado con los nuevos valores <br />
        ///<b>Entrada:</b> clsListaVideojuego oListaVideojuego. El videojuego a editar de la lista de videojuegos <br />
        ///<b>Salida:</b> int resultado. El número de filas afectadas, el resultado esperado debería ser 4 (porque se modifican 4 filas debido a los triggers) <br />
        /// </summary>
        /// <param name="oListaVideojuego"><b>oListaVideojuego - clsListaVideojuego. </b> El videojuego a editar de la lista de videojuegos, se editara el registro que coincida con los ids del objeto pasado por parametros</param>
        /// <returns><b>resultado - int.</b>El número de filas borradas en la BBDD como resultado de la instrucción, el resultado correcto es 4 (porque se modifican 4 filas debido a los triggers), cualquier otro resultado puede significar un error</returns>
        public int editarVideojuegoDeListaBL(clsListaVideojuego oListaVideojuego)
        {
            return new clsGestoraListaVideojuegosDAL().editarVideojuegoDeListaDAL(oListaVideojuego);
        }
        /// <summary>
        ///<b>Cabecera:</b> public int insertarVideojuegoEnListaBL(clsListaVideojuego oVideojuegoEnLista) <br />
        ///<b>Descripción:</b> Este metodo se encarga de llamar a la capa DAL y que esta inserte un videojuego en la lista de un usuario de la BBDD <br />
        ///<b> Precondiciones:</b> El usuario y el videojuego deben existir, el juego no puede existir previamente en la lista del usuario <br />
        ///<b> Postcondiciones:</b> Se insertara el videojuego en la lista del usuario <br />
        ///<b>Entrada:</b> clsListaVideojuego oVideojuegoEnLista. El videojuego y la lista en la que se desea insertar el videojuego <br />
        ///<b>Salida:</b> int resultado. El numero de filas afectadas por la insercion <br />
        /// </summary>
        /// <param name="oVideojuegoEnLista"><b>oVideojuegoEnLista - clsListaVideojuego. </b> El videojuego y la lista en la que se desea insertar</param>
        /// <returns><b>resultado - int.</b>El numero de filas afectadas por la instruccion de insercion. El resultado esperado es 4 (porque se modifican 4 filas debido a los triggers), cualquier otro resultado podria significar error</returns>
        public int insertarVideojuegoEnListaBL(clsListaVideojuego oVideojuegoEnLista)
        {
            return new clsGestoraListaVideojuegosDAL().insertarVideojuegoEnListaDAL(oVideojuegoEnLista);
        }
    }
}
