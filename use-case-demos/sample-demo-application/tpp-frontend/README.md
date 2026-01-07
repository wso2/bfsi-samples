# TPP Demo Application

This is a static demo web application that demonstrates the basics of the WSO2 Open Banking platform. This app allows users to experience how a real Third-Party Provider (TPP), which is connected to the WSO2 Open Banking platform, would function.

---

## Components

This application contains two major components:

-   **TPP Application**: This is the front-end application that simulates a third-party finance aggregator. It's the part the user interacts with to initiate a banking process.

-   **Banking Pages**: These are mock banking pages that represent the bank's interface. This is where the user would be redirected to authenticate and authorize transactions or data sharing with the TPP.

---

## Configuration

All configurations for this demo application are stored in the `config.json` file. You can modify this file to adjust settings related to the WSO2 Open Banking platform, such as endpoints and client credentials, to match your specific setup.

---

## Getting Started

### Required Tool Versions

* **Node.js**: The recommended build tool is `npm`, which is bundled with Node.js. It's best to use the latest LTS (Long-Term Support) version of Node.js for stability. Tested node version is `v22.17.0`.

* **Vite**: This project is built using Vite, a fast front-end build tool.

### Steps to Start the App

**Install Dependencies**: Navigate to the project's root directory in your terminal and install all required dependencies.

```bash
npm install
```
**Start the Development Server**: Once the dependencies are installed, you can start the development server.

```bash
npm run dev
```
The Vite development server will start, and the application will be accessible at a local address, usually `http://localhost:5173`. The terminal will provide the exact URL.

**Open in Browser**: Open your web browser and navigate to the URL provided in the terminal to view and interact with the TPP demo application.