/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import { useCallback, useEffect, useState, type ReactNode } from "react";
import { Box, Chip, Typography } from "@mui/material";

/** One step of the Open Banking flow, as the backend recorded it. */
export interface FlowEntry {
    id: number;
    label: string;
    timestamp: string;
    request: {
        method: string;
        url: string;
        headers?: string;
        body?: string;
    };
    response?: {
        status?: number;
        body: string;
        isError?: boolean;
    };
    decodedPayload?: string;
}

const CONSOLE_URL = `${import.meta.env.BASE_URL}init/dev-console`;

/**
 * Only polled while the panel is open. The log lives in the session, not in the page, so a flow
 * run with the panel closed is still there in full when it is opened afterwards.
 */
const POLL_MS = 1500;

const CODE_BG = "#1a2332";
const PANEL_BG = "#546e7a";
const TAB_BG = "#455a64";
const BORDER = "#37474f";

/**
 * @component DevConsole
 * @description A slide-out panel showing every call this application made to the Identity Server
 * and the bank APIs while serving the current session: what was sent, what came back, and what
 * the JWTs involved actually said.
 *
 * Unlike a browser network panel it shows the calls the *backend* makes, which is where the whole
 * Open Banking flow happens - the browser only ever sees this application's own `/init/*`
 * endpoints. The log is kept in the HTTP session, so it survives the trip to the authorization
 * server and back, and one customer never sees another's tokens.
 */
const DevConsole = () => {
    const [open, setOpen] = useState(false);
    const [entries, setEntries] = useState<FlowEntry[]>([]);
    const [selectedId, setSelectedId] = useState<number | null>(null);

    const refresh = useCallback(async () => {
        try {
            const response = await fetch(CONSOLE_URL, { headers: { Accept: "application/json" } });
            if (!response.ok) {
                return;
            }
            const payload = await response.json();
            setEntries(payload.entries ?? []);
        } catch (e) {
            // The panel is a diagnostic aid; a failed poll must never disturb the application.
            console.debug("dev console poll failed", e);
        }
    }, []);

    const clear = useCallback(async () => {
        try {
            await fetch(CONSOLE_URL, { method: "DELETE" });
        } catch (e) {
            console.debug("dev console clear failed", e);
        }
        setSelectedId(null);
        setEntries([]);
    }, []);

    useEffect(() => {
        if (!open) {
            return undefined;
        }
        refresh();
        const timer = window.setInterval(refresh, POLL_MS);
        return () => window.clearInterval(timer);
    }, [open, refresh]);

    const latest = entries.length > 0 ? entries[entries.length - 1] : null;
    const selected =
        (selectedId != null ? entries.find((entry) => entry.id === selectedId) : undefined) ?? latest;

    return (
        <Box
            sx={{
                position: "fixed",
                top: 0,
                right: 0,
                height: "100vh",
                display: "flex",
                flexDirection: "row",
                zIndex: 1300,
                pointerEvents: "none",
            }}
        >
            {open && (
                <Box
                    sx={{
                        width: { xs: "100vw", md: 720 },
                        backgroundColor: PANEL_BG,
                        display: "flex",
                        flexDirection: "column",
                        pointerEvents: "all",
                        boxShadow: "-4px 0 16px rgba(0,0,0,0.35)",
                    }}
                >
                    <Box
                        sx={{
                            px: 2,
                            py: 1.2,
                            backgroundColor: TAB_BG,
                            borderBottom: `1px solid ${BORDER}`,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "space-between",
                            gap: 2,
                        }}
                    >
                        <Typography
                            sx={{
                                color: "#eceff1",
                                fontFamily: "monospace",
                                fontSize: 13,
                                letterSpacing: 1.5,
                                fontWeight: 700,
                            }}
                        >
                            Developer Console
                        </Typography>
                        <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                            <Typography sx={{ color: "#90a4ae", fontFamily: "monospace", fontSize: 11 }}>
                                {entries.length} call{entries.length !== 1 ? "s" : ""}
                            </Typography>
                            <Typography
                                onClick={clear}
                                sx={{
                                    color: "#cfd8dc",
                                    fontFamily: "monospace",
                                    fontSize: 11,
                                    cursor: "pointer",
                                    userSelect: "none",
                                    "&:hover": { color: "#fff", textDecoration: "underline" },
                                }}
                            >
                                clear
                            </Typography>
                        </Box>
                    </Box>

                    <Box sx={{ maxHeight: "34%", overflowY: "auto", borderBottom: `2px solid ${BORDER}` }}>
                        {entries.length === 0 ? (
                            <Typography
                                sx={{
                                    color: "#90a4ae",
                                    textAlign: "center",
                                    my: 3,
                                    px: 3,
                                    fontSize: 12,
                                    fontFamily: "monospace",
                                }}
                            >
                                No calls captured yet. Link an account or make a payment.
                            </Typography>
                        ) : (
                            entries.map((entry) => (
                                <Box
                                    key={entry.id}
                                    onClick={() => setSelectedId(entry.id)}
                                    sx={{
                                        px: 1.5,
                                        py: 0.7,
                                        cursor: "pointer",
                                        borderBottom: `1px solid ${BORDER}`,
                                        backgroundColor:
                                            selected?.id === entry.id ? "#37474f" : "transparent",
                                        "&:hover": { backgroundColor: "#3d5360" },
                                        display: "flex",
                                        alignItems: "center",
                                        gap: 1,
                                    }}
                                >
                                    <Chip
                                        label={entry.request.method}
                                        size="small"
                                        sx={{
                                            fontFamily: "monospace",
                                            fontSize: 9,
                                            height: 18,
                                            flexShrink: 0,
                                            backgroundColor:
                                                entry.request.method === "POST" ? "#bf360c" : "#1565c0",
                                            color: "white",
                                        }}
                                    />
                                    <Typography
                                        sx={{
                                            color: "#eceff1",
                                            fontSize: 12,
                                            fontFamily: "monospace",
                                            flex: 1,
                                            overflow: "hidden",
                                            textOverflow: "ellipsis",
                                            whiteSpace: "nowrap",
                                        }}
                                    >
                                        {entry.label}
                                    </Typography>
                                    <Typography
                                        sx={{
                                            color: "#78909c",
                                            fontSize: 10,
                                            fontFamily: "monospace",
                                            flexShrink: 0,
                                        }}
                                    >
                                        {new Date(entry.timestamp).toLocaleTimeString()}
                                    </Typography>
                                    {entry.response != null && (
                                        <Chip
                                            label={entry.response.status ?? (entry.response.isError ? "ERR" : "OK")}
                                            size="small"
                                            sx={{
                                                fontFamily: "monospace",
                                                fontSize: 9,
                                                height: 18,
                                                flexShrink: 0,
                                                backgroundColor: entry.response.isError ? "#b71c1c" : "#1b5e20",
                                                color: "white",
                                            }}
                                        />
                                    )}
                                </Box>
                            ))
                        )}
                    </Box>

                    <Box sx={{ flex: 1, display: "flex", minHeight: 0 }}>
                        <Pane title="Request" borderRight>
                            {selected ? formatRequest(selected) : ""}
                        </Pane>
                        <Pane title="Response" borderRight={Boolean(selected?.decodedPayload)}
                              color={selected?.response?.isError ? "#ef9a9a" : undefined}>
                            {selected?.response?.body || "—"}
                        </Pane>
                        {selected?.decodedPayload && (
                            <Pane title="Decoded JWT Payload" color="#a5d6a7">
                                {selected.decodedPayload}
                            </Pane>
                        )}
                    </Box>
                </Box>
            )}

            <Box
                onClick={() => setOpen((isOpen) => !isOpen)}
                sx={{
                    width: 34,
                    backgroundColor: open ? BORDER : TAB_BG,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    cursor: "pointer",
                    pointerEvents: "all",
                    borderLeft: `1px solid ${BORDER}`,
                    "&:hover": { backgroundColor: BORDER },
                    transition: "background-color 0.15s",
                    flexShrink: 0,
                }}
            >
                <Typography
                    sx={{
                        color: "#cfd8dc",
                        fontSize: 10,
                        fontWeight: 700,
                        letterSpacing: 2.5,
                        writingMode: "vertical-rl",
                        transform: "rotate(180deg)",
                        userSelect: "none",
                        fontFamily: "monospace",
                    }}
                >
                    DEVELOPER CONSOLE
                </Typography>
            </Box>
        </Box>
    );
};

/** One titled, scrollable, monospaced column of the panel. */
const Pane = ({
    title,
    children,
    borderRight = false,
    color = "#cdd3de",
}: {
    title: string;
    children: ReactNode;
    borderRight?: boolean;
    color?: string;
}) => (
    <Box
        sx={{
            flex: 1,
            minWidth: 0,
            display: "flex",
            flexDirection: "column",
            borderRight: borderRight ? `1px solid ${BORDER}` : "none",
        }}
    >
        <Typography
            sx={{
                color: "#90a4ae",
                fontFamily: "monospace",
                fontSize: 11,
                px: 1.5,
                py: 0.6,
                borderBottom: `1px solid ${BORDER}`,
                backgroundColor: TAB_BG,
                flexShrink: 0,
            }}
        >
            {title}
        </Typography>
        <Box sx={{ flex: 1, overflowY: "auto", p: 1.5, backgroundColor: CODE_BG }}>
            <pre
                style={{
                    margin: 0,
                    color,
                    fontSize: 11,
                    fontFamily: "monospace",
                    whiteSpace: "pre-wrap",
                    wordBreak: "break-all",
                    lineHeight: 1.65,
                }}
            >
                {children}
            </pre>
        </Box>
    </Box>
);

/**
 * Renders a captured call the way a request is written: the line, then the headers, then the
 * body. The backend has already unpacked form bodies and decoded the JWTs inside them.
 *
 * @param entry the captured step
 * @returns the request as displayable text
 */
const formatRequest = (entry: FlowEntry): string => {
    const lines: string[] = [`${entry.request.method} ${entry.request.url}`];
    if (entry.request.headers) {
        lines.push(entry.request.headers);
    }
    if (entry.request.body) {
        lines.push("", entry.request.body);
    }
    return lines.join("\n");
};

export default DevConsole;
