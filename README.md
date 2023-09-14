# sdwebui_api_bot
Java app to automatically process txt2img json file and make request to A1111 Stable Diffusion Web UI API.

At startup, the app will:
1. create json_input, image_output, and config folders
2. load default config if application.properties doesn't exist in the config folder
3. create a sample txt2img input file if one doesn't exist in json_input folder
4. the app will continue to generate and save new images in image_out folder until stopped

#### enhancements:
* log4j
* controlnet support
