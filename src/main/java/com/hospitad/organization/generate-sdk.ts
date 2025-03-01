import fs from "fs";
import path from "path";

const pluginsDir = path.join(__dirname, "../lib/devX/plugins");

if (!fs.existsSync(pluginsDir)) {
    fs.mkdirSync(pluginsDir, { recursive: true });
}

// Generate TypeScript clients dynamically
apis.forEach(async (api) => {
    console.log(`Generating SDK for ${api.name}...`);
    // makeFetch()
    // const schema = await generateSchema(api.input);

    const outputPath = path.join(pluginsDir, `${api.name}.ts`);
    const content = `
    import { createClient, FetchConfig } from "openapi-fetch";

    const BASE = "${api.baseUrl}";
    const TOKEN = process.env.${api.authEnv};

    export const ${api.name} = createClient<FetchConfig>({
      baseUrl: BASE,
      headers: {
        Authorization: TOKEN ? \`Bearer \${TOKEN}\` : undefined,
        "Content-Type": "application/json",
      },
      schema: ${JSON.stringify(schema)},
    });
  `;

    fs.writeFileSync(outputPath, content.trim());
    console.log(`✅ SDK for ${api.name} generated successfully!`);
});
