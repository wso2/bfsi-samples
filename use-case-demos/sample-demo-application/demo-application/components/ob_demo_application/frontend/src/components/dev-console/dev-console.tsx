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

import { useCallback, useEffect, useState } from "react";
import { Box, Chip, Typography } from "@mui/material";

export interface FlowEntry {
    id: number;
    timestamp: string;
    label: string;
    durationMs: number;
    request: {
        method: string;
        url: string;
        headers?: Record<string, string>;
        body?: string;
    };
    response: {
        status: number;
        isError: boolean;
        body?: string;
    };
    decoded?: Record<string, string>;
}

const PANEL_BG = "#263238";
const TAB_BG = "#37474f";
const CODE_BG = "#1a2332";
const BORDER = "#455a64";
const MUTED = "#90a4ae";
const POLL_MS = 2000;

const backendBase = `${window.location.origin}/${window.location.pathname.split("/")[1]}`;

const paneLabel = {
    color: MUTED,
    fontFamily: "monospace",
    fontSize: 11,
    px: 1.5,
    py: 0.6,
    borderBottom: `1px solid ${BORDER}`,
    backgroundColor: TAB_BG,
    flexShrink: 0,
};

const preStyle: React.CSSProperties = {
    margin: 0,
    color: "#cfd8dc",
    fontFamily: "monospace",
    fontSize: 11,
    whiteSpace: "pre-wrap",
    wordBreak: "break-all",
};

/**
 * Formats a JSON string for display, falling back to the raw text when it is not JSON.
 */
const pretty = (text?: string): string => {
    if (!text) {
        return "(empty)";
    }
    try {
        return JSON.stringify(JSON.parse(text), null, 2);
    } catch {
        return text;
    }
};

/**
 * A collapsible panel showing the Open Banking requests the backend made, with decoded JWTs.
 * Data is polled from the backend, since these calls happen server side over mTLS and are
 * never visible in the browser's own network tab.
 */
export default function DevConsole() {
    const [open, setOpen] = useState(false);
    const [entries, setEntries] = useState<FlowEntry[]>([]);
    const [selectedId, setSelectedId] = useState<number | null>(null);
    const [tab, setTab] = useState<"request" | "response" | "decoded">("request");

    const load = useCallback(async () => {
        try {
            const res = await fetch(`${backendBase}/init/dev-console`, {
                headers: { Accept: "application/json" },
            });
            if (res.ok) {
                setEntries(await res.json());
            }
        } catch {
            /* console is best effort; ignore transport errors */
        }
    }, []);

    useEffect(() => {
        load();
        const t = setInterval(load, POLL_MS);
        return () => clearInterval(t);
    }, [load]);

    const clear = async () => {
        try {
            await fetch(`${backendBase}/init/dev-console`, { method: "DELETE" });
        } catch {
            /* ignore */
        }
        setEntries([]);
        setSelectedId(null);
    };

    const selected =
        (selectedId != null ? entries.find((e) => e.id === selectedId) : undefined) ??
        entries[entries.length - 1] ??
        null;

    const decodedKeys = selected?.decoded ? Object.keys(selected.decoded) : [];

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
                        width: 660,
                        maxWidth: "95vw",
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
                            Open Banking Requests
                        </Typography>
                        <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                            <Typography sx={{ color: MUTED, fontFamily: "monospace", fontSize: 11 }}>
                                {entries.length} call{entries.length !== 1 ? "s" : ""}
                            </Typography>
                            <Typography
                                onClick={clear}
                                sx={{
                                    color: MUTED,
                                    fontFamily: "monospace",
                                    fontSize: 11,
                                    cursor: "pointer",
                                    "&:hover": { color: "#eceff1" },
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
                                    color: MUTED,
                                    textAlign: "center",
                                    my: 3,
                                    fontSize: 12,
                                    fontFamily: "monospace",
                                }}
                            >
                                No Open Banking calls captured yet.
                                <br />
                                Try &quot;Add Account&quot; or make a payment.
                            </Typography>
                        ) : (
                            entries.map((entry) => (
                                <Box
                                    key={entry.id}
                                    onClick={() => setSelectedId(entry.id)}
                                    sx={{
                                        px: 1.5,
                                        py: 0.9,
                                        display: "flex",
                                        alignItems: "center",
                                        gap: 1,
                                        cursor: "pointer",
                                        borderBottom: `1px solid ${BORDER}`,
                                        backgroundColor:
                                            selected?.id === entry.id ? "#1a2332" : "transparent",
                                        "&:hover": { backgroundColor: "#2f3f47" },
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
                                            backgroundColor: "#455a64",
                                            color: "#eceff1",
                                        }}
                                    />
                                    <Typography
                                        sx={{
                                            color: "#cfd8dc",
                                            fontFamily: "monospace",
                                            fontSize: 11,
                                            flex: 1,
                                            overflow: "hidden",
                                            textOverflow: "ellipsis",
                                            whiteSpace: "nowrap",
                                        }}
                                    >
                                        {entry.label}
                                    </Typography>
                                    <Typography
                                        sx={{ color: MUTED, fontFamily: "monospace", fontSize: 10, flexShrink: 0 }}
                                    >
                                        {entry.durationMs}ms
                                    </Typography>
                                    <Chip
                                        label={entry.response.isError ? `ERR ${entry.response.status}` : entry.response.status}
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
                                </Box>
                            ))
                        )}
                    </Box>

                    <Box sx={{ display: "flex", backgroundColor: TAB_BG, flexShrink: 0 }}>
                        {(["request", "response", "decoded"] as const).map((name) => (
                            <Typography
                                key={name}
                                onClick={() => setTab(name)}
                                sx={{
                                    px: 2,
                                    py: 0.7,
                                    cursor: "pointer",
                                    fontFamily: "monospace",
                                    fontSize: 11,
                                    color: tab === name ? "#eceff1" : MUTED,
                                    borderBottom: tab === name ? "2px solid #4fc3f7" : "2px solid transparent",
                                }}
                            >
                                {name}
                                {name === "decoded" && decodedKeys.length > 0 ? ` (${decodedKeys.length})` : ""}
                            </Typography>
                        ))}
                    </Box>

                    <Box sx={{ flex: 1, overflowY: "auto", backgroundColor: CODE_BG, p: 1.5, minHeight: 0 }}>
                        {!selected ? (
                            <Typography sx={{ color: MUTED, fontFamily: "monospace", fontSize: 11 }}>
                                Select a call above.
                            </Typography>
                        ) : tab === "request" ? (
                            <>
                                <Typography sx={{ ...paneLabel, mb: 1 }}>
                                    {selected.request.method} {selected.request.url}
                                </Typography>
                                <pre style={preStyle}>
                                    {Object.entries(selected.request.headers ?? {})
                                        .map(([k, v]) => `${k}: ${v}`)
                                        .join("\n")}
                                    {"\n\n"}
                                    {pretty(selected.request.body)}
                                </pre>
                            </>
                        ) : tab === "response" ? (
                            <pre style={preStyle}>{pretty(selected.response.body)}</pre>
                        ) : decodedKeys.length === 0 ? (
                            <Typography sx={{ color: MUTED, fontFamily: "monospace", fontSize: 11 }}>
                                No JWTs in this exchange.
                            </Typography>
                        ) : (
                            decodedKeys.map((key) => (
                                <Box key={key} sx={{ mb: 2 }}>
                                    <Typography
                                        sx={{ color: "#4fc3f7", fontFamily: "monospace", fontSize: 11, mb: 0.5 }}
                                    >
                                        {key}
                                    </Typography>
                                    <pre style={preStyle}>{selected.decoded?.[key]}</pre>
                                </Box>
                            ))
                        )}
                    </Box>
                </Box>
            )}

            <Box
                onClick={() => setOpen(!open)}
                sx={{
                    pointerEvents: "all",
                    cursor: "pointer",
                    backgroundColor: TAB_BG,
                    color: "#eceff1",
                    writingMode: "vertical-rl",
                    px: 0.6,
                    py: 2,
                    mt: 10,
                    height: "fit-content",
                    borderRadius: "4px 0 0 4px",
                    fontFamily: "monospace",
                    fontSize: 12,
                    letterSpacing: 1.5,
                    userSelect: "none",
                    boxShadow: "-2px 0 8px rgba(0,0,0,0.3)",
                }}
            >
                {open ? "CLOSE ›" : "‹ OB REQUESTS"}
            </Box>
        </Box>
    );
}
