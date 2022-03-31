using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_Entidades
{
    public class clsUsuario
    {
        private int id;
        private string nickname;
        private string contrasenya;

        public clsUsuario()
        {
            id = 0;
            nickname = "Usuario anónimo";
            contrasenya = "";
        }
        public clsUsuario(int id, string nickname, string contrasenya)
        {
            this.id = id;
            this.nickname = nickname;
            this.contrasenya = contrasenya;
        }

        public int Id { get => id; set => id = value; }
        public string Nickname { get => nickname; set => nickname = value; }
        public string Contrasenya { get => contrasenya; set => contrasenya = value; }
    }
}
