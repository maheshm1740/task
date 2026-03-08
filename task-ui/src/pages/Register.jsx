import { useState } from "react";
import client from "../api/httpClient";
import { useNavigate, Link } from "react-router-dom";

export default function Register(){

  const [name,setName] = useState("");
  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const navigate = useNavigate();

  const handleRegister = async (e)=>{

    e.preventDefault();

    try{

      await client.post("/users",{name,email,password});

      alert("User registered");

      navigate("/");

    }catch(err){

      alert("Registration failed");

    }

  };

  return(

    <div className="container mt-5">

      <div className="row justify-content-center">

        <div className="col-md-4">

          <div className="card p-4">

            <h3 className="text-center mb-3">Register</h3>

            <form onSubmit={handleRegister}>

              <input
                className="form-control mb-3"
                placeholder="Name"
                onChange={(e)=>setName(e.target.value)}
              />

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

              <button className="btn btn-success w-100">
                Register
              </button>

            </form>

            {/* Back to Login */}
            <div className="text-center mt-3">
              <Link to="/" className="text-primary">
                ← Back to Login
              </Link>
            </div>

          </div>

        </div>

      </div>

    </div>

  );
}