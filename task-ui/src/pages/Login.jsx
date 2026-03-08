import { useState } from "react";
import client from "../api/httpClient";
import { useNavigate, Link } from "react-router-dom";

export default function Login(){

  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e)=>{

    e.preventDefault();

    try{

      const res = await client.post("/users/login",{email,password});

      localStorage.setItem("token",res.data.data);
      console.log(res.data.data)
      navigate("/dashboard");

    }catch(err){

      alert("Login failed");

    }

  };

  return(

    <div className="container mt-5">

      <div className="row justify-content-center">

        <div className="col-md-4">

          <div className="card p-4">

            <h3 className="text-center mb-3">Login</h3>

            <form onSubmit={handleLogin}>

              <input
                className="form-control mb-3"
                placeholder="Email"
                onChange={(e)=>setEmail(e.target.value)}
              />

              <input
                type="password"
                className="form-control mb-3"
                placeholder="Password"
                onChange={(e)=>setPassword(e.target.value)}
              />

              <button className="btn btn-primary w-100">
                Login
              </button>

            </form>

            <p className="mt-3 text-center">
              <Link to="/register">Register</Link>
            </p>

          </div>

        </div>

      </div>

    </div>

  );
}