import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '1', target: 2},
		{ duration: '2', target: 3},
		{ duration: '4', target: 5},
 	],
};

export default function () {
 const BASE_URL = 'https://www.youtube.com/watch?v=z_UNpD0AvFc'

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}