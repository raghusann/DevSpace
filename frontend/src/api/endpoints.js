const ENDPOINTS = {
  AUTH: {
    LOGIN: "/api/auth/login",
    REGISTER: "/api/auth/register",
    REFRESH: "/api/auth/refresh",
    LOGOUT: "/api/auth/logout",
    FORGOT_PASSWORD: "/api/auth/forgot-password",
    RESET_PASSWORD: "/api/auth/reset-password",
    VERIFY_EMAIL: "/api/auth/verify-email",
  },

  DASHBOARD: {
    STATS: "/api/dashboard/stats",
  },

  PROJECTS: {
    BASE: "/api/projects",
    BY_ID: (id) => `/api/projects/${id}`,
    MEMBERS: (id) => `/api/projects/${id}/members`,
  },

  KANBAN: {
    BOARDS: (projectId) =>
      `/api/projects/${projectId}/kanban/boards`,

    BOARD: (projectId, boardId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}`,

    COLUMNS: (projectId, boardId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/columns`,

      UPDATE_COLUMN: (projectId, boardId, columnId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/columns/${columnId}`,

    DELETE_COLUMN: (projectId, boardId, columnId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/columns/${columnId}`,

    TASKS: (projectId, boardId, columnId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/columns/${columnId}/tasks`,

    TASK: (projectId, boardId, taskId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/tasks/${taskId}`,

    MOVE_TASK: (projectId, boardId, taskId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/tasks/${taskId}/move`,

    COMMENTS: (projectId, boardId, taskId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/tasks/${taskId}/comments`,

    DELETE_TASK: (projectId, boardId, taskId) =>
      `/api/projects/${projectId}/kanban/boards/${boardId}/tasks/${taskId}`,
  },

  PROFILE: {
    BASE: "/api/profile",
    PASSWORD: "/api/profile/password",
    ACTIVITY: "/api/profile/activity",
  },

  API_TESTER: {
    COLLECTIONS: "/api/apitester/collections",

    REQUESTS: (collectionId) =>
      `/api/apitester/collections/${collectionId}/requests`,

    ENVIRONMENTS: "/api/apitester/environments",

    PROXY: "/api/apitester/proxy",
  },

  GITHUB: {
    STATUS: "/api/github/status",
    CONNECT: "/api/github/connect",

    PROJECT_REPOS: (projectId) =>
      `/api/github/projects/${projectId}/repos`,
  },

  DOCKER: {
    CONTAINERS: "/api/docker/containers",
    IMAGES: "/api/docker/images",

    LOGS: (id) =>
      `/api/docker/containers/${id}/logs`,
  },

  DEPLOYMENTS: {
    BASE: (projectId) =>
      `/api/projects/${projectId}/deployments`,
  },

  CHAT: {
    ROOMS: (projectId) =>
      `/api/projects/${projectId}/chat/rooms`,

    MESSAGES: (projectId, roomId) =>
      `/api/projects/${projectId}/chat/rooms/${roomId}/messages`,
  },

  DOCUMENTS: {
    BASE: (projectId) =>
      `/api/projects/${projectId}/documents`,
  },

  DATABASE: {
    TABLES: (projectId) =>
      `/api/projects/${projectId}/database/tables`,

    QUERY: (projectId) =>
      `/api/projects/${projectId}/database/query`,

    CONFIG: (projectId) =>
      `/api/projects/${projectId}/database/config`,
  },

  ADMIN: {
    USERS: "/api/admin/users",
    ANALYTICS: "/api/admin/analytics",
    AUDIT_LOGS: "/api/admin/audit-logs",
  },
};

export default ENDPOINTS;