package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.Admin;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.dto.AdminDto;
import cat.institutmarianao.sailing.ws.model.dto.ClientDto;
import cat.institutmarianao.sailing.ws.model.dto.UserDto;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

	@Override
	public UserDto convert(User user) {
		if (user instanceof Admin admin) {
			AdminDto adminDto = new AdminDto();
			BeanUtils.copyProperties(admin, adminDto);
			return adminDto;
		}
		if (user instanceof Client client) {
			ClientDto clientDto = new ClientDto();
			BeanUtils.copyProperties(client, clientDto);
			return clientDto;
		}
		return null;
	}
}
