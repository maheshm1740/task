export function getUserRole() {

  const token = localStorage.getItem("token");

  if (!token) return null;

  const payload = JSON.parse(atob(token.split(".")[1]));

  return payload.role || null;

}

export function logout() {
  localStorage.removeItem("token");
  window.location.href = "/";
}