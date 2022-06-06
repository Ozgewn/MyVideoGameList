﻿using MVGL_DAL.Conexion;
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
        /// <summary>
        /// <b>Cabecera:</b> public bool isNombreUsuarioExistenteDAL(String nombreUsuario) <br />
        /// <b>Descripción:</b> Este metodo se encarga de comprobar si existe un usuario con exactamente el
        /// mismo nombre de usuario que el nombreUsuario introducido por parámetros <br />
        /// <b>Precondiciones:</b> N/A <br />
        /// <b>Postcondiciones:</b> Se devolverá un booleano: true si existe, false si no existe <br />
        /// <b>Entrada:</b> String nombreUsuario. El nombre de usuario del cual se desea comprobar la existencia en la BBDD <br />
        /// <b>Salida:</b> bool nombreUsuarioExiste. true si existe, false si no existe <br />
        /// </summary>
        /// <param name="nombreUsuario"><b>nombreUsuario - String. </b>El nombre de usuario que se desea buscar en la BBDD para comprobar si existe o no</param>
        /// <returns>nombreUsuarioExiste - bool. true si existe el usuario con dicho nombre de usuario, false si no existe el usuario en cuestion</returns>
        public bool isNombreUsuarioExistenteDAL(String nombreUsuario)
        {
            bool nombreUsuarioExiste = false;
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.Parameters.Add("@nombreUsuario", System.Data.SqlDbType.VarChar).Value = nombreUsuario;
            miComando.CommandText = "SELECT * FROM Usuarios WHERE NombreUsuario = @nombreUsuario";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows) //si tiene filas, significa que ha encontrado al usuario en cuestion
                {
                    nombreUsuarioExiste=true;
                }
            }
            catch (Exception)
            {
                throw;
            }

            return nombreUsuarioExiste;
        }
        /// <summary>
        /// <b>Cabecera:</b> public List<clsUsuario> getListadoCompletoUsuariosDAL() <br />
        /// <b>Descripción:</b> Este método se encarga de devolver una lista de usuarios completa<br />
        /// <b>Precondiciones:</b>Se debe tener conexión a Internet, debe existir 1 o más usuarios en la BBDD<br />
        /// <b>Postcondiciones:</b> Se devolvera un listado completo con todos los usuarios encontrados en la BBDD <br />
        /// <b>Salida:</b> List<clsUsuario> oListadoCompletoUsuarios. Un listado completo de usuarios<br />
        /// </summary>
        /// <returns><b>oListadoCompletoUsuarios - List<clsUsuario>. </b>Un listado completo de usuarios</returns>
        public List<clsUsuario> getListadoCompletoUsuariosDAL()
        {
            List<clsUsuario> oListadoCompletoUsuarios = new List<clsUsuario>();
            clsUsuario oUsuario;
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.CommandText = "SELECT * FROM Usuarios";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows)
                {
                    while (miLector.Read())
                    {
                        oUsuario = new clsUsuario();
                        oUsuario.Id = (String)miLector["Id"];
                        oUsuario.NombreUsuario = (String)miLector["NombreUsuario"];
                        oUsuario.VideojuegosJugados = (int)miLector["VideojuegosJugados"]; //no controlo nulos porque la BBDD no permite que aqui hayan nulos, y la BBDD lo controla poniendo 0 como valor por defecto
                        oUsuario.VideojuegosPlaneados = (int)miLector["VideojuegosPlaneados"];
                        oUsuario.VideojuegosDropeados = (int)miLector["VideojuegosDropeados"];
                        oUsuario.VideojuegosEnPausa = (int)miLector["VideojuegosEnPausa"];
                        oUsuario.VideojuegosJugando = (int)miLector["VideojuegosJugando"];
                        oUsuario.EsListaPrivada = (bool)miLector["esListaPrivada"];
                        oListadoCompletoUsuarios.Add(oUsuario);
                    } 
                }
                miLector.Close();
                conexionBase.closeConnection(ref conexionEstablecida);
            }
            catch (SqlException)
            {
                throw;
            }
            return oListadoCompletoUsuarios;
        }
    }
}
