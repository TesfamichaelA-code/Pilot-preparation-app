export default () => ({
  port: parseInt(process.env.PORT, 10) || 5000,
  database: {
    uri: process.env.MONGODB_URI || 'mongodb://localhost:27017/pilot-preparation',
  },
  jwt: {
    secret: process.env.JWT_SECRET || 'super-secret-pilot-prep-jwt-key',
    expiresIn: process.env.JWT_EXPIRES_IN || '1d',
  },
});
