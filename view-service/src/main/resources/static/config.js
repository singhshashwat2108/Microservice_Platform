const HOST = window.location.hostname || 'localhost';

const API = {
  AUTH: `http://${HOST}:8081`,
  QUERY: `http://${HOST}:8082`,
  VIEW: `http://${HOST}:8083`
};

function getToken() {
  return sessionStorage.getItem('token');
}

function getUsername() {
  return sessionStorage.getItem('username');
}

function getUserId() {
  return sessionStorage.getItem('userId');
}

function authHeaders() {
  const token = getToken();
  return token
    ? { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` }
    : { 'Content-Type': 'application/json' };
}

async function parseError(res) {
  try {
    const data = await res.json();
    return data.message || 'Request failed.';
  } catch (_) {
    return 'Request failed.';
  }
}
