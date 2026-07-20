import { BrowserRouter, Navigate, Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";

import ProtectedRoute from "./ProtectedRoute";
import DashboardLayout from "../layouts/DashboardLayout";

import LoginPage from "../pages/auth/LoginPage";
import RegisterPage from "../pages/auth/RegisterPage";
import DashboardPage from "../pages/dashboard/DashboardPage";
import ProjectsPage from "../pages/projects/ProjectsPage";
import KanbanPage from "../pages/kanban/KanbanPage";
import ApiTesterPage from "../pages/apitester/ApiTesterPage";
import GitHubPage from "../pages/github/GitHubPage";
import DockerPage from "../pages/docker/DockerPage";
import DeploymentsPage from "../pages/deployments/DeploymentsPage";
import ChatPage from "../pages/chat/ChatPage";
import DatabasePage from "../pages/database/DatabasePage";
import DocsPage from "../pages/docs/DocsPage";
import ProfilePage from "../pages/profile/ProfilePage";
import AdminPage from "../pages/admin/AdminPage";
import NotFoundPage from "../pages/errors/NotFoundPage";

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Toaster position="top-right" />
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<DashboardLayout />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/projects" element={<ProjectsPage />} />
            <Route path="/projects/:projectId/kanban" element={<KanbanPage />} />
            <Route path="/api-tester" element={<ApiTesterPage />} />
            <Route path="/github" element={<GitHubPage />} />
            <Route path="/docker" element={<DockerPage />} />
            <Route path="/deployments" element={<DeploymentsPage />} />
            <Route path="/chat" element={<ChatPage />} />
            <Route path="/database" element={<DatabasePage />} />
            <Route path="/docs" element={<DocsPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/admin" element={<AdminPage />} />
          </Route>
        </Route>

        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}
