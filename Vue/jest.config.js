module.exports = {
  moduleFileExtensions: ["js", "jsx", "json", "vue"],
  setupFiles: ["./setup.js"],
  transform: {
    "^.+\\.vue$": "vue-jest",
    "^.+\\.js?$": "babel-jest"
  },
  transformIgnorePatterns: ["node_modules/"],
  moduleNameMapper: {
    "^@/(.*)$": "<rootDir>/src/$1"
  },
};
