import { useEffect, useState } from "react";
import client from "../api/httpClient";
import { logout, getUserRole } from "../api/auth";
import TaskForm from "../components/TaskForm";

export default function Dashboard() {

  const [tasks, setTasks] = useState([]);
  const [role, setRole] = useState(null);
  const [editingTaskId, setEditingTaskId] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const loadTasks = async (userRole) => {

  let endpoint = "/tasks?page=0&size=10";

  if (userRole === "ADMIN") {
    endpoint = "/tasks/admin/all?page=0&size=10";
  }

  const res = await client.get(endpoint);

  setTasks(res.data.data.content);
};

  const deleteTask = async (id) => {
    await client.delete(`/tasks/${id}`);
    loadTasks();
  };

  useEffect(() => {
    const userRole = getUserRole();
    setRole(userRole);
    loadTasks(userRole);
  }, []);

  const openAddForm = () => {
    setEditingTaskId(null);
    setShowForm(true);
  };

  const openEditForm = (id) => {
    setEditingTaskId(id);
    setShowForm(true);
  };

  const closeForm = () => {
    setShowForm(false);
  };

  const handleSuccess = () => {
    closeForm();
    loadTasks();
  };

  return (
    <div className="container mt-5">

      <div className="d-flex justify-content-between mb-4">
        <h2>Task Dashboard</h2>

        <div>
          <button
            className="btn btn-primary me-2"
            onClick={openAddForm}
          >
            Add Task
          </button>

          <button
            className="btn btn-danger"
            onClick={logout}
          >
            Logout
          </button>
        </div>
      </div>

      {showForm && (
        <TaskForm
          taskId={editingTaskId}
          onSuccess={handleSuccess}
          onCancel={closeForm}
        />
      )}

      <div className="row mt-4">

        {tasks.map(task => (
          <div key={task.id} className="col-md-4 mb-3">

            <div className="card p-3">

              <h5>{task.title}</h5>
              <p>{task.description}</p>

              <span className="badge bg-secondary mb-2">
                {task.status}
              </span>

              <div>

                {role === "USER" && (
                  <button
                    className="btn btn-warning btn-sm me-2"
                    onClick={() => openEditForm(task.id)}
                  >
                    Update
                  </button>
                )}

                {role === "ADMIN" && (
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => deleteTask(task.id)}
                  >
                    Delete
                  </button>
                )}

              </div>

            </div>

          </div>
        ))}

      </div>

    </div>
  );
}