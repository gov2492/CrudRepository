package com.project.crud.crud.config;

import com.mongodb.*;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@Configuration
@EnableMongoAuditing
public class MongoDBConfig {


	@Value("${spring.data.mongodb.database}")
	private String mongoDBName;

	@Value("${mongodb.host}")
	private String mongodbHost;

	@Value("${mongodb.port}")
	private int mongodbPort;

	@Value("${mongodb.host.slave}")
	private String mongodbHostSecondary;

	@Value("${mongodb.port.slave}")
	private int mongodbPortSecondary;

	@Value("${mongodb.connectTimeoutMS}")
	private int mongoTimeout;

	@Value("${mongodb.allow.authentication}")
	private boolean allowAuthentication;

	@Value("${mongodb.username}")
	private String mongodbUser;

	@Value("${mongodb.password}")
	private String mongodbPass;

	@Value("${mongodb.loggingenabled}")
	private Boolean mongodbLoggingEnabled;

	@Value("${mongodb.is.read.primary}")
	private boolean readPrimary;

	private List<ServerAddress> seeds;
	private MongoCredential cred;
	//private StringBuilder connString;

	@PostConstruct
	public void init() {
		//if ("prod".equals(airtelAppConfig.getProfile()))
		//	initProd();
		//else
		initDev();
	}

	public void initDev() {
		MongoClientOptions.Builder builder = MongoClientOptions.builder().connectTimeout(mongoTimeout);
		if (StringUtils.isNotBlank(mongodbHostSecondary)) {
			builder.readPreference(ReadPreference.secondaryPreferred());
		}
		builder.build();

		cred = MongoCredential.createCredential(mongodbUser, mongoDBName, mongodbPass.toCharArray());
		seeds = new ArrayList<>();

		ServerAddress primary = new ServerAddress(mongodbHost, mongodbPort);
		seeds.add(primary);

		if (StringUtils.isNotBlank(mongodbHostSecondary)) {
			for (String ip : mongodbHostSecondary.split(",")) {
				seeds.add(new ServerAddress(ip, mongodbPortSecondary));
			}
		}
	}

	public void initProd() {
		MongoClientOptions.Builder builder = MongoClientOptions.builder().connectTimeout(mongoTimeout);
		if (StringUtils.isNotBlank(mongodbHostSecondary)) {
			builder.readPreference(ReadPreference.secondaryPreferred());
		}
		builder.build();

		cred = MongoCredential.createCredential(mongodbUser, mongoDBName, mongodbPass.toCharArray());
		seeds = new ArrayList<>();

		ServerAddress primary = new ServerAddress(mongodbHost, mongodbPort);
		seeds.add(primary);

		if (StringUtils.isNotBlank(mongodbHostSecondary)) {
			for (String ip : mongodbHostSecondary.split(",")) {
				seeds.add(new ServerAddress(ip, mongodbPortSecondary));
			}
		}
	}


	@Configuration
	@Transactional
	@EnableReactiveMongoRepositories("com.project.crud.crud")
	private class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {

		private MongoClient mongo;

		public ReactiveMongoConfiguration() {
		}

		@PreDestroy
		public void close() {
			if (this.mongo != null) {
				this.mongo.close();
			}
		}

		@Bean
		@Override
		public MongoClient reactiveMongoClient() {
			this.mongo = MongoClients.create(mongoClientSettings());
			return this.mongo;
		}

		//"mongodb://host1:27017,host2:27017,host3:27017/?replicaSet=myReplicaSet"
		private MongoClientSettings mongoClientSettings() {
			return MongoClientSettings.builder().applyToClusterSettings(clusterSettingsBuilder()).retryWrites(Boolean.TRUE)
				//.applyToSslSettings(sslSettingsBuilder()).credential(cred).build();
				.applyToSslSettings(sslSettingsBuilder()).build();
		}

		private Block<ClusterSettings.Builder> clusterSettingsBuilder() {
			return (cluster) -> cluster.hosts(seeds);
		}

		private Block<SslSettings.Builder> sslSettingsBuilder() {
			return (ssl) -> ssl.enabled(false).build();
		}

		@Override
		protected String getDatabaseName() {
			return mongoDBName;
		}

		/**
		 * This method is used for '.' convertor because mongo in key doesn't allow '.'
		 *
		 * @param mongoConverter
		 */
		@Autowired
		public void setMapKeyDotReplacement(MappingMongoConverter mongoConverter) {
			mongoConverter.setMapKeyDotReplacement("#");
		}
	}
}
