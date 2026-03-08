import { useEffect, useState } from "react";
import client from "../api/httpClient";

export default function TaskForm({ taskId, onSuccess, onCancel }) {

  const isEdit = !!taskId;

  const [form, setForm] = useState({
    title: "",
    description: "",
    status: "PENDING"
  });

  useEffect(() => {
    if (isEdit) {
      loadTask();
    }
  }, [taskId]);

  const loadTask = async () => {
    const res = await client.get(`/tasks/${taskId}`);
    setForm(res.data.data);
  };

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (isEdit) {
      await client.put(`/tasks/${taskId}`, form);
    } else {
      await client.post("/tasks", form);
    }

    onSuccess();
  };

  return (
    <div className="card p-4">

      <h4 className="mb-3">
        {isEdit ? "Update Task" : "Add Task"}
      </h4>

      <form onSubmit={handleSubmit}>

        <div className="mb-3">
          <label className="form-label">Title</label>
          <input
            className="form-control"
            name="title"
            value={form.title}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Description</label>
          <textarea
            className="form-control"
            name="description"
            value={form.description || ""}
            onChange={handleChange}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Status</label>

          <select
            className="form-select"
            name="status"
            value={form.status}
            onChange={handleChange}
          >
            <option value="PENDING">PENDING</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="COMPLETED">COMPLETED</option>
          </select>

        </div>

        <button className="btn btn-success me-2">
          {isEdit ? "Update" : "Create"}
        </button>

        <button
          type="button"
          className="btn btn-secondary"
          onClick={onCancel}
        >
          Cancel
        </button>

      </form>

    </div>
  );
}