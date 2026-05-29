/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{js,jsx}"] ,
  theme: {
    extend: {
      colors: {
        ink: "#0b0d12",
        slate: "#1f2937",
        haze: "#f4f1ec",
        accent: "#f97316",
        mint: "#2dd4bf",
        sunrise: "#ffd6a5",
        dusk: "#ffe1d2"
      },
      fontFamily: {
        heading: ["Fraunces", "ui-serif", "Georgia"],
        body: ["Sora", "ui-sans-serif", "system-ui"]
      },
      boxShadow: {
        glow: "0 18px 50px rgba(249, 115, 22, 0.28)",
        soft: "0 12px 30px rgba(15, 23, 42, 0.08)"
      }
    }
  },
  plugins: []
};
