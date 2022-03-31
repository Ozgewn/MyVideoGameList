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
        public List<clsUsuario> getUsuarioSegunId(int id)
        {
            clsUsuario oUsuario = new clsUsuario();
            SqlCommand miComando = new SqlCommand();
            SqlDataReader miLector;
            miComando.Parameters.Add("@id", System.Data.SqlDbType.Int).Value = id;
            miComando.CommandText = "SELECT * FROM Usuarios WHERE Id = @id";
            conexionEstablecida = conexionBase.getConnection();
            miComando.Connection = conexionEstablecida;
            miLector = miComando.ExecuteReader();

            try
            {
                if (miLector.HasRows)
                {
                    miLector.Read();
                    if (miLector["IDPersona"] != System.DBNull.Value)
                    {
                        oPersona.Id = (int)miLector["IDPersona"];
                    }
                    if (miLector["nombrePersona"] != System.DBNull.Value)
                    {
                        oPersona.Nombre = (String)miLector["nombrePersona"];
                    }
                    if (miLector["apellidosPersona"] != System.DBNull.Value)
                    {
                        oPersona.Apellidos = (String)miLector["apellidosPersona"];
                    }
                    if (miLector["fechaNacimiento"] != System.DBNull.Value)
                    {
                        oPersona.FechaNacimiento = (DateTime)miLector["fechaNacimiento"];
                    }
                    if (miLector["direccion"] != System.DBNull.Value)
                    {
                        oPersona.Direccion = (String)miLector["direccion"];
                    }
                    if (miLector["telefono"] != System.DBNull.Value)
                    {
                        oPersona.Telefono = (String)miLector["telefono"];
                    }
                    if (miLector["Foto"] != System.DBNull.Value)
                    {
                        oPersona.UrlFoto = (String)miLector["Foto"];
                    }
                    else
                    {
                        oPersona.UrlFoto = FOTO_POR_DEFECTO;
                    }
                    if (miLector["IDDepartamento"] != System.DBNull.Value)
                    {
                        oPersona.IdDepartamento = (int)miLector["IDDepartamento"]; //realmente, aqui no hace falta controlar el nulo de iddepartamento ya que la vista no permite que este sea nulo, pero lo controlo por asi acaso
                    }
                }
                miLector.Close();
                conexionBase.closeConnection(ref conexionEstablecida);
            }
            catch (SqlException)
            {
                throw;
            }
            return oPersona;
        }
    }
}
